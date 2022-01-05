package bg.codeacademy.spring.gossiptalks.exception;

public class UserNotFoundException extends RuntimeException{

  public UserNotFoundException() {
    super("User not found!");
  }
}
