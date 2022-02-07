package graphql.resolvers

import javax.inject.Inject
import models.Post
import repositories.PostRepositoryImpl

import scala.concurrent.{ExecutionContext, Future}

class PostResolver @Inject()(postRepository: PostRepositoryImpl,
                             implicit val executionContext: ExecutionContext) {

  def postsAll: Future[List[Post]] = postRepository.findAll()

  def addPost(post: Post): Future[Post] = postRepository.create(post)

  def findPost(id: Long): Future[Option[Post]] = postRepository.find(id)

  def updatePost(post: Post): Future[Post] = postRepository.update(post)

  def deletePost(id: Long): Future[Option[Post]] = postRepository.delete(id)
}
