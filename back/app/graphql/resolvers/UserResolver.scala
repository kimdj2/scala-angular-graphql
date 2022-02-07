package graphql.resolvers

import javax.inject.Inject
import models.User
import repositories.UserRepositoryImpl

import scala.concurrent.{ExecutionContext, Future}

class UserResolver @Inject()(userRepository: UserRepositoryImpl,
                             implicit val executionContext: ExecutionContext) {

  def usersAll: Future[List[User]] = userRepository.findAll()

  def addUser(user: User): Future[User] = userRepository.create(user)

  def findUser(id: Long): Future[Option[User]] = userRepository.find(id)

  def updateUser(user: User): Future[User] = userRepository.update(user)

  def deleteUser(id: Long): Future[Option[User]] = userRepository.delete(id)
}
