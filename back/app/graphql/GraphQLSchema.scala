package graphql

import javax.inject.Inject
import graphql.schemas.{PostSchema, UserSchema}
import sangria.schema.{ObjectType, fields}

class GraphQLSchema @Inject()(
  postSchema: PostSchema,
  userSchema: UserSchema
) {

  /**
    * Contains a GraphQL schema for the entire application.
    * You can add more queries, mutations, and subscriptions for each model.
    */
  val Schema = sangria.schema.Schema(
    query = ObjectType(
      "Query",
      fields(
        postSchema.Queries ++ 
        userSchema.Queries: _*
      )
    ),
    mutation = Some(
      ObjectType(
        "Mutation",
        fields(
          postSchema.Mutations ++ 
          userSchema.Mutations: _*
        )
      )
    )
  )
}
