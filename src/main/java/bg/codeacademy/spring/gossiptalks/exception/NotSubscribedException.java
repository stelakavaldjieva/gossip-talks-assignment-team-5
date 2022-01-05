package bg.codeacademy.spring.gossiptalks.exception;

public class NotSubscribedException extends RuntimeException
{
  public NotSubscribedException()
  {
    super("Subscribe first!");
  }
}
