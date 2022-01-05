package bg.codeacademy.spring.gossiptalks.exception;

public class UserAlreadyExistsException extends RuntimeException{

  public UserAlreadyExistsException(String message) {
    super(message);
  }

  public UserAlreadyExistsException() {
    super("Failed - the user already exists.");


  }
}
