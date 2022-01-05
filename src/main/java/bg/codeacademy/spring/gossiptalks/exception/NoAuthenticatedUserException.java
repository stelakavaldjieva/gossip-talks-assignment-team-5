package bg.codeacademy.spring.gossiptalks.exception;

public class NoAuthenticatedUserException extends RuntimeException
{
  public NoAuthenticatedUserException()
  {
    super("There is no authenticated user!");
  }
}
