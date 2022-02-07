package graphql.schemas

import akka.stream.Materializer
import javax.inject.Inject
import graphql.resolvers.UserResolver
import models.User
import sangria.macros.derive.{ObjectTypeName, deriveObjectType}
import sangria.schema._

import scala.concurrent.ExecutionContext

class UserSchema @Inject()(userResolver: UserResolver)(implicit ec: ExecutionContext, mat: Materializer) {

  implicit val UserType: ObjectType[Unit, User] = deriveObjectType[Unit, User](ObjectTypeName("User"))

  object FieldNames extends Enumeration {

    val usersAll:   Value = Value("usersAll")
    val addUser:    Value = Value("addUser")
    val findUser:   Value = Value("findUser")
    val deleteUser: Value = Value("deleteUser")
    val editUser:   Value = Value("editUser")

    implicit def valueToString(value: Value): String = value.toString
  }

  import FieldNames._

  val Queries: List[Field[Unit, Unit]] = List(
    Field(
      name = usersAll,
      fieldType = ListType(UserType),
      resolve = _ => userResolver.usersAll
    ),
    Field(
      name = findUser,
      fieldType = OptionType(UserType),
      arguments = List(
        Argument("id", LongType)
      ),
      resolve = ctx => userResolver.findUser(ctx.args.arg[Long]("id"))
    )
  )

  val Mutations: List[Field[Unit, Unit]] = List(
    Field(
      name = addUser,
      fieldType = UserType,
      arguments = List(
        Argument("name", StringType),
        Argument("tel", StringType),
        Argument("email", StringType)
      ),
      resolve = ctx =>
        userResolver.addUser(
          User(
            None,
            ctx.args.arg[String]("name"),
            ctx.args.arg[String]("tel"), 
            ctx.args.arg[String]("email")
          )
        )
    ),
    Field(
      name = editUser,
      fieldType = UserType,
      arguments = List(
        Argument("id", LongType),
        Argument("name", StringType),
        Argument("tel", StringType),
        Argument("email", StringType)
      ),
      resolve = ctx =>
        userResolver.updateUser(
          User(
            Some(ctx.args.arg[Long]("id")),
            ctx.args.arg[String]("name"),
            ctx.args.arg[String]("tel"), 
            ctx.args.arg[String]("email")
          )
        )
    ),
    Field(
      name = deleteUser,
      fieldType = OptionType(UserType),
      arguments = List(
        Argument("id", LongType)
      ),
      resolve = ctx => {
        val userId = ctx.args.arg[Long]("id")
        userResolver.deleteUser(userId)
      }
    )
  )
}