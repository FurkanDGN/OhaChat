package com.outlook.furkan.dogan.dev.ohachat.common.domain.exception;

/**
 * @author Furkan Doğan
 */
public class UnsupportedTierException extends RuntimeException {

  public UnsupportedTierException() {
  }

  public UnsupportedTierException(String message) {
    super(message);
  }

  public UnsupportedTierException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnsupportedTierException(Throwable cause) {
    super(cause);
  }

  public UnsupportedTierException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
