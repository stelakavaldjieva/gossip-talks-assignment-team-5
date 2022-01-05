package bg.codeacademy.spring.gossiptalks.exception;

public class PasswordsDoNotMatchException extends RuntimeException{

  public PasswordsDoNotMatchException() {
    super("Passwords do not match!");
  }

  public PasswordsDoNotMatchException(String message) {
    super(message);
  }
}
