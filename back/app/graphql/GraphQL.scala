package graphql

import javax.inject.Inject
import graphql.schemas.PostSchema
import sangria.schema.{ObjectType, fields}

/**
  * A basic component for a GraphQL schema.
  *
  * @param postSchema an object containing all queries, mutations, and subscriptions to work with the Post entity
  */
class GraphQL @Inject()(postSchema: PostSchema) {

  /**
    * Contains a GraphQL schema for the entire application.
    * You can add more queries, mutations, and subscriptions for each model.
    */
  val Schema = sangria.schema.Schema(
    query = ObjectType(
      "Query",
      fields(
        postSchema.Queries: _*
      )
    ),
    mutation = Some(
      ObjectType(
        "Mutation",
        fields(
          postSchema.Mutations: _*
        )
      )
    )
  )
}
