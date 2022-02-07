package repositories

import models.User
import scala.concurrent.Future

trait UserRepository {
  def create(post: User): Future[User]
  def find(id: Long):     Future[Option[User]]
  def findAll():          Future[List[User]]
  def update(post: User): Future[User]
  def delete(id: Long):   Future[Option[User]]
}