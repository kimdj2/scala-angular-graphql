package graphql.schemas

import akka.stream.Materializer
import javax.inject.Inject
import graphql.resolvers.PostResolver
import models.Post
import sangria.macros.derive.{ObjectTypeName, deriveObjectType}
import sangria.schema._

import scala.concurrent.ExecutionContext

class PostSchema @Inject()(postResolver: PostResolver)(implicit ec: ExecutionContext, mat: Materializer) {

  implicit val PostType: ObjectType[Unit, Post] = deriveObjectType[Unit, Post](ObjectTypeName("Post"))

  object FieldNames extends Enumeration {

    val postsAll: Value = Value("postsAll")
    val addPost: Value = Value("addPost")
    val findPost: Value = Value("findPost")
    val deletePost: Value = Value("deletePost")
    val editPost: Value = Value("editPost")
    val postsUpdated: Value = Value("postsUpdated")

    implicit def valueToString(value: Value): String = value.toString
  }

  import FieldNames._

  val Queries: List[Field[Unit, Unit]] = List(
    Field(
      name = postsAll,
      fieldType = ListType(PostType),
      resolve = _ => postResolver.postsAll
    ),
    Field(
      name = findPost,
      fieldType = OptionType(PostType),
      arguments = List(
        Argument("id", LongType)
      ),
      resolve = ctx => postResolver.findPost(ctx.args.arg[Long]("id"))
    )
  )

  val Mutations: List[Field[Unit, Unit]] = List(
    Field(
      name = addPost,
      fieldType = PostType,
      arguments = List(
        Argument("title", StringType),
        Argument("content", StringType)
      ),
      resolve = ctx =>
        postResolver.addPost(
          Post(
            None,
            ctx.args.arg[String]("title"),
            ctx.args.arg[String]("content")
          )
        )
    ),
    Field(
      name = editPost,
      fieldType = PostType,
      arguments = List(
        Argument("id", LongType),
        Argument("title", StringType),
        Argument("content", StringType)
      ),
      resolve = ctx =>
        postResolver.updatePost(
          Post(
            Some(ctx.args.arg[Long]("id")),
            ctx.args.arg[String]("title"),
            ctx.args.arg[String]("content")
          )
        )
    ),
    Field(
      name = deletePost,
      fieldType = OptionType(PostType),
      arguments = List(
        Argument("id", LongType)
      ),
      resolve = ctx => {
        val postId = ctx.args.arg[Long]("id")
        postResolver.deletePost(postId)
      }
    )
  )
}