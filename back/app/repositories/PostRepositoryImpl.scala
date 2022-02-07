package repositories

import javax.inject.{Inject, Singleton}
import errors.{AlreadyExists, AmbiguousResult, NotFound}
import models.Post
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PostRepositoryImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                                  (implicit val executionContext: ExecutionContext) extends PostRepository with HasDatabaseConfigProvider[JdbcProfile] {

  val DB = dbConfig.db

  import dbConfig.profile.api._

  def postQuery = TableQuery[Post.PostTable]

  override def create(post: Post): Future[Post] = DB.run {
    Actions.create(post)
  }

  override def find(id: Long): Future[Option[Post]] = DB.run {
    Actions.find(id)
  }

  override def findAll(): Future[List[Post]] = DB.run {
    Actions.findAll()
  }

  override def update(post: Post): Future[Post] = DB.run {
    Actions.update(post)
  }

  override def delete(id: Long): Future[Option[Post]] = DB.run {
    Actions.delete(id)
  }

  object Actions {

    def create(post: Post): DBIO[Post] =
      for {
        targetPost   <- post.id.fold[DBIO[Option[Post]]](DBIO.successful(None))(find)
        targetPostId <- targetPost match {
          case Some(_) => DBIO.failed(AlreadyExistsException(s"Post id: ${post.id}"))
          case _       => postQuery returning postQuery.map(_.id) += post
        }
        targetPost <- find(targetPostId)
        post       <- targetPost match {
          case Some(value) => DBIO.successful(value)
          case _           => DBIO.failed(DBException("Create Fail"))
        }
      } yield post


    def find(id: Long): DBIO[Option[Post]] = for {
      targetPost <- postQuery.filter(_.id === id).result
      post       <- if (targetPost.lengthCompare(2) < 0) DBIO.successful(targetPost.headOption)
                    else DBIO.failed(DBException("Find Fail"))
    } yield post


    def findAll(): DBIO[List[Post]] = for {
      posts <- postQuery.result
    } yield posts.toList

    def update(post: Post): DBIO[Post] = for {
      targetId <- post.id.fold[DBIOAction[Long, _, Effect]](DBIO.failed(NotFoundException("Not found id: ${post.id}")))(DBIO.successful)
      count    <- postQuery.filter(_.id === targetId).update(post)
      result   <- count match {
        case 0 => DBIO.failed(NotFoundException("Update Fail"))
        case _ => DBIO.successful(post)
      }
    } yield result

    def delete(id: Long): DBIO[Option[Post]] = for {
      targetPost   <- find(id)
      targetDelete <- targetPost match {
        case Some(_) => postQuery.filter(_.id === id).delete
        case _       => DBIO.failed(NotFoundException("Delete Fail"))
      }
      result <- targetDelete match {
        case 1 => DBIO.successful(targetPost)
        case _ => DBIO.failed(DBException("Delete Fail"))
      }
    } yield result
  }

}
