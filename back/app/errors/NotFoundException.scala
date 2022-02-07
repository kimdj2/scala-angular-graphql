package errors

import sangria.execution.UserFacingError

case class NotFoundException(msg: String) extends Exception(msg) with UserFacingError {
  override def getMessage: String = msg
}
