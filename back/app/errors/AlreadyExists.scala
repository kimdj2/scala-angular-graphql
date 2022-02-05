package errors

import sangria.execution.UserFacingError

/**
  * Represents an exception object indicating that an object already exists.
  *
  * @param msg an exception message
  */
case class AlreadyExists(msg: String) extends Exception with UserFacingError {
  override def getMessage: String = msg
}
