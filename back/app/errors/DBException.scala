package errors

import sangria.execution.UserFacingError

case class DBException(msg: String) extends Exception with UserFacingError {
  override def getMessage: String = msg
}
