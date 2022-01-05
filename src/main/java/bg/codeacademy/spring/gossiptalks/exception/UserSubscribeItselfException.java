package bg.codeacademy.spring.gossiptalks.exception;

public class UserSubscribeItselfException extends RuntimeException{

  public UserSubscribeItselfException() {
    super("You cannot follow your account!");
  }
}
