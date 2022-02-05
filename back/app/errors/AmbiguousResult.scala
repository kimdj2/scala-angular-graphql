package errors

import sangria.execution.UserFacingError

/**
  * Represents an exception object indicating that an ambiguous result was received.
  *
  * @param msg an exception message
  */
case class AmbiguousResult(msg: String) extends Exception with UserFacingError {
  override def getMessage: String = msg
}
