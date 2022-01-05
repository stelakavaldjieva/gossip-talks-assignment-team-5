package bg.codeacademy.spring.gossiptalks.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckForHTMLValidator.class)
public @interface CheckForHTML
{
  String message() default "You wrote forbidden symbols!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
