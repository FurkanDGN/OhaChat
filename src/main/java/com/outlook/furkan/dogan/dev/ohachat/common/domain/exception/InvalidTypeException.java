package com.outlook.furkan.dogan.dev.ohachat.common.domain.exception;

/**
 * @author Furkan Doğan
 */
public class InvalidTypeException extends RuntimeException {

  public InvalidTypeException() {
  }

  public InvalidTypeException(String message) {
    super(message);
  }

  public InvalidTypeException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidTypeException(Throwable cause) {
    super(cause);
  }

  public InvalidTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
