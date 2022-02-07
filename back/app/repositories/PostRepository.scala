package repositories

import models.Post
import scala.concurrent.Future

trait PostRepository {
  def create(post: Post): Future[Post]
  def find(id: Long):     Future[Option[Post]]
  def findAll():          Future[List[Post]]
  def update(post: Post): Future[Post]
  def delete(id: Long):   Future[Option[Post]]
}