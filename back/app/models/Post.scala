package models

import slick.lifted.{Tag => SlickTag}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

/**
  * The main entity over which the CRUD operations are carried out.
  *
  * @param id      an entity ID
  * @param title   a post title
  * @param content a post content
  */
case class Post(id: Option[Long] = None, title: String, content: String)

/**
  * Defines a Slick table for the Post entity.
  */
object Post extends ((Option[Long], String, String) => Post) {

  class PostTable(slickTag: SlickTag) extends Table[Post](slickTag, "POSTS") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def title = column[String]("TITLE")

    def content = column[String]("CONTENT")

    def * = (id.?, title, content).mapTo[Post]
  }
}
