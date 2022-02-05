package errors

import sangria.execution.UserFacingError

/**
  * Represents an exception object indicating that an object wasn't found.
  *
  * @param msg an exception message
  */
case class NotFound(msg: String) extends Exception(msg) with UserFacingError {
  override def getMessage(): String = msg
}
