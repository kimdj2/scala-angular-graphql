package graphql.resolvers

import javax.inject.Inject
import models.Post
import repositories.PostRepositoryImpl

import scala.concurrent.{ExecutionContext, Future}

/**
  * A resolver class that contains all resolver methods for the Post model.
  *
  * @param postRepository   a repository that provides basic operations for the Post entity
  * @param executionContext execute program logic asynchronously, typically but not necessarily on a thread pool
  */
class PostResolver @Inject()(postRepository: PostRepositoryImpl,
                             implicit val executionContext: ExecutionContext) {

  /**
    * Returns a list of all posts.
    *
    * @return the list of posts
    */
  def postsAll: Future[List[Post]] = postRepository.findAll()

  /**
    * Adds and saves a new Post instance.
    *
    * @param title   a post title
    * @param content a post content
    * @return the created Post instance
    */
  def addPost(post: Post): Future[Post] = postRepository.create(post)

  /**
    * Finds a post by ID.
    *
    * @param id a post ID
    * @return the found Post instance
    */
  def findPost(id: Long): Future[Option[Post]] = postRepository.find(id)

  /**
    * Updates an existing post.
    *
    * @param post a post to be updated
    * @return the updated Post instance
    */
  def updatePost(post: Post): Future[Post] = postRepository.update(post)

  /**
    * Deletes an existing post.
    *
    * @param id a post ID
    * @return a boolean value
    */
  def deletePost(id: Long): Future[Option[Post]] = postRepository.delete(id)
}
