package models

import slick.lifted.{Tag => SlickTag}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

case class Post(
  id:      Option[Long] = None, 
  title:   String, 
  content: String
)

object Post extends ((Option[Long], String, String) => Post) {

  class PostTable(slickTag: SlickTag) extends Table[Post](slickTag, "post") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def title = column[String]("title")

    def content = column[String]("content")

    def * = (id.?, title, content).mapTo[Post]
  }
}
