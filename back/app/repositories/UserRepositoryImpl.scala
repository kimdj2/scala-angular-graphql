package repositories

import javax.inject.{Inject, Singleton}
import errors.{ AlreadyExistsException, DBException, NotFoundException }
import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepositoryImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                                  (implicit val executionContext: ExecutionContext) extends UserRepository with HasDatabaseConfigProvider[JdbcProfile] {

  val DB = dbConfig.db

  import dbConfig.profile.api._

  def userQuery = TableQuery[User.UserTable]

  
  override def create(user: User): Future[User] = DB.run {
    Actions.create(user)
  }

  override def find(id: Long): Future[Option[User]] = DB.run {
    Actions.find(id)
  }

  override def findAll(): Future[List[User]] = DB.run {
    Actions.findAll()
  }

  override def update(user: User): Future[User] = DB.run {
    Actions.update(user)
  }

  override def delete(id: Long): Future[Option[User]] = DB.run {
    Actions.delete(id)
  }

  object Actions {

    def create(user: User): DBIO[User] =
      for {
        targetUser   <- user.id.fold[DBIO[Option[User]]](DBIO.successful(None))(find)
        targetUserId <- targetUser match {
          case Some(_) => DBIO.failed(AlreadyExistsException(s"User id: ${user.id}"))
          case _       => userQuery returning userQuery.map(_.id) += user
        }
        targetUser <- find(targetUserId)
        user       <- targetUser match {
          case Some(value) => DBIO.successful(value)
          case _           => DBIO.failed(DBException("Create Fail"))
        }
      } yield user


    def find(id: Long): DBIO[Option[User]] = for {
      targetUser <- userQuery.filter(_.id === id).result
      user <- if (targetUser.lengthCompare(2) < 0) DBIO.successful(targetUser.headOption)
      else DBIO.failed(DBException("Find Fail"))
    } yield user


    def findAll(): DBIO[List[User]] = for {
      users <- userQuery.result
    } yield users.toList

    def update(user: User): DBIO[User] = for {
      targetId <- user.id.fold[DBIOAction[Long, _, Effect]](DBIO.failed(NotFoundException("Not found id: ${user.id}")))(DBIO.successful)
      count    <- userQuery.filter(_.id === targetId).update(user)
      result   <- count match {
        case 0 => DBIO.failed(NotFoundException("Update Fail"))
        case _ => DBIO.successful(user)
      }
    } yield result

    def delete(id: Long): DBIO[Option[User]] = for {
      targetUser   <- find(id)
      targetDelete <- targetUser match {
        case Some(_) => userQuery.filter(_.id === id).delete
        case _       => DBIO.failed(NotFoundException("Delete Fail"))
      }
      result <- targetDelete match {
        case 1 => DBIO.successful(targetUser)
        case _ => DBIO.failed(DBException("Delete Fail"))
      }
    } yield result
  }

}
