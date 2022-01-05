package bg.codeacademy.spring.gossiptalks.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler
{

  //Format Exception Body for custom exceptions.
  private Map<String, String> getExceptionBody(RuntimeException ex)
  {
    Map<String, String> exceptionBody = new HashMap<>();
    exceptionBody.put("timestamp", LocalDateTime.now().toString());
    exceptionBody.put("error", ex.getMessage());
    return exceptionBody;
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex)
  {
    return ResponseEntity.badRequest().body(getExceptionBody(ex));
  }

  @ExceptionHandler(NotSubscribedException.class)
  public ResponseEntity<Object> handleNotSubscribedException(NotSubscribedException ex)
  {
    return ResponseEntity.badRequest().body(getExceptionBody(ex));
  }

  @ExceptionHandler(UserSubscribeItselfException.class)
  public ResponseEntity<Object> handleUserSubscribeItselfException(UserSubscribeItselfException ex)
  {
    return ResponseEntity.badRequest().body(getExceptionBody(ex));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex)
  {
    return ResponseEntity.badRequest().body(getExceptionBody(ex));
  }

  @ExceptionHandler(PasswordsDoNotMatchException.class)
  public ResponseEntity<Object> handlePasswordsDoNotMatchException(PasswordsDoNotMatchException ex)
  {
    return ResponseEntity.badRequest().body(getExceptionBody(ex));
  }

  @ExceptionHandler(NoAuthenticatedUserException.class)
  public ResponseEntity<Object> handleNoAuthenticatedUserException(NoAuthenticatedUserException ex)
  {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(getExceptionBody(ex));
  }

  @Override
  protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers,
                                                       HttpStatus status, WebRequest request)
  {
    Map<String, String> exceptionBody = new HashMap<>();
    exceptionBody.put("timestamp", LocalDateTime.now().toString());
    exceptionBody.put("error", "Invalid fields!");

    List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();

    for (ObjectError objectError : objectErrors) {
      if (objectError instanceof FieldError) {
        exceptionBody.put(((FieldError) objectError).getField(), objectError.getDefaultMessage());
      }
      else {
        exceptionBody.put(objectError.getObjectName(), objectError.getDefaultMessage());
      }
    }

    return ResponseEntity.badRequest().body(exceptionBody);
  }
}
