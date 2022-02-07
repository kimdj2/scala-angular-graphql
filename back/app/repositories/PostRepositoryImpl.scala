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
        maybePost <- post.id.fold[DBIO[Option[Post]]](DBIO.successful(None))(find)
        maybePostId <- maybePost match {
          case Some(_) => DBIO.failed(AlreadyExists(s"Post id: ${post.id} already exists"))
          case _ => postQuery returning postQuery.map(_.id) += post
        }
        maybePost <- find(maybePostId)
        post <- maybePost match {
          case Some(value) => DBIO.successful(value)
          case _ => DBIO.failed(AmbiguousResult(s"Failed to save the post [post=$post]"))
        }
      } yield post


    def find(id: Long): DBIO[Option[Post]] = for {
      maybePost <- postQuery.filter(_.id === id).result
      post <- if (maybePost.lengthCompare(2) < 0) DBIO.successful(maybePost.headOption)
      else DBIO.failed(AmbiguousResult(s"Several posts with the same id = $id"))
    } yield post


    def findAll(): DBIO[List[Post]] = for {
      posts <- postQuery.result
    } yield posts.toList

    def update(post: Post): DBIO[Post] = for {
      maybeId <- post.id.fold[DBIOAction[Long, _, Effect]](DBIO.failed(NotFound(s"Not found 'id' in the [post=$post]")))(DBIO.successful)
      count <- postQuery.filter(_.id === maybeId).update(post)
      result <- count match {
        case 0 => DBIO.failed(NotFound(s"Cannot find a post with the ID=${post.id.get}"))
        case _ => DBIO.successful(post)
      }
    } yield result

    def delete(id: Long): DBIO[Option[Post]] = for {
      maybePost <- find(id)
      maybeDelete <- maybePost match {
        case Some(_) => postQuery.filter(_.id === id).delete
        case _ => DBIO.failed(NotFound(s"Cannot find a post with [ID=$id]"))
      }
      result <- maybeDelete match {
        case 1 => DBIO.successful(maybePost)
        case _ => DBIO.failed(AmbiguousResult(s"Failed to delete the post with the [ID=$id]"))
      }
    } yield result
  }

}
