package models

import slick.lifted.{Tag => SlickTag}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

case class User(
  id:    Option[Long] = None, 
  name:  String, 
  tel:   String, 
  email: String
)

object User extends ((Option[Long], String, String, String) => User) {
  class UserTable(slickTag: SlickTag) extends Table[User](slickTag, "user") {
    def id    = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name  = column[String]("name")
    def tel   = column[String]("tel")
    def email = column[String]("email")
    def *     = (id.?, name, tel, email).mapTo[User]
  }
}
