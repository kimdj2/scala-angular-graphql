package repositories

import javax.inject.{Inject, Singleton}
import errors.{AlreadyExists, AmbiguousResult, NotFound}
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
        maybeUser   <- user.id.fold[DBIO[Option[User]]](DBIO.successful(None))(find)
        maybeUserId <- maybeUser match {
          case Some(_) => DBIO.failed(AlreadyExists(s"User id: ${user.id} already exists"))
          case _       => userQuery returning userQuery.map(_.id) += user
        }
        maybeUser <- find(maybeUserId)
        user      <- maybeUser match {
          case Some(value) => DBIO.successful(value)
          case _           => DBIO.failed(AmbiguousResult(s"Failed to save the user [user=$user]"))
        }
      } yield user


    def find(id: Long): DBIO[Option[User]] = for {
      maybeUser <- userQuery.filter(_.id === id).result
      user      <- if (maybeUser.lengthCompare(2) < 0) DBIO.successful(maybeUser.headOption)
                   else DBIO.failed(AmbiguousResult(s"Several users with the same id = $id"))
    } yield user


    def findAll(): DBIO[List[User]] = for {
      users <- userQuery.result
    } yield users.toList

    def update(user: User): DBIO[User] = for {
      maybeId <- user.id.fold[DBIOAction[Long, _, Effect]](DBIO.failed(NotFound(s"Not found 'id' in the [user=$user]")))(DBIO.successful)
      count   <- userQuery.filter(_.id === maybeId).update(user)
      result  <- count match {
        case 0 => DBIO.failed(NotFound(s"Cannot find a user with the ID=${user.id.get}"))
        case _ => DBIO.successful(user)
      }
    } yield result

    def delete(id: Long): DBIO[Option[User]] = for {
      maybeUser   <- find(id)
      maybeDelete <- maybeUser match {
        case Some(_) => userQuery.filter(_.id === id).delete
        case _       => DBIO.failed(NotFound(s"Cannot find a user with [ID=$id]"))
      }
      result <- maybeDelete match {
        case 1 => DBIO.successful(maybeUser)
        case _ => DBIO.failed(AmbiguousResult(s"Failed to delete the user with the [ID=$id]"))
      }
    } yield result
  }

}
