package repositories

import models.Post
import scala.concurrent.Future

/**
  * A simple repository which provides basic CRUD operations.
  */
trait PostRepository {

  /**
    * Saves an instance of Post to the database.
    *
    * @param post the new instance
    * @return the saved instance
    */
  def create(post: Post): Future[Post]

  /**
    * Returns a post instance found by ID.
    *
    * @param id the instance ID
    * @return the found instance
    */
  def find(id: Long): Future[Option[Post]]

  /**
    * Returns a list of post instances.
    *
    * @return the list of posts
    */
  def findAll(): Future[List[Post]]

  /**
    * Updates an existing post instance.
    *
    * @param post the new instance
    * @return the updated instance
    */
  def update(post: Post): Future[Post]

  /**
    * Deletes an existing instance found by ID.
    *
    * @param id the instance ID
    * @return the deleted post instance
    */
  def delete(id: Long): Future[Option[Post]]
}