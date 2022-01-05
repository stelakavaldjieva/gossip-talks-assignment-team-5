package bg.codeacademy.spring.gossiptalks.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckForHTMLValidator implements ConstraintValidator<CheckForHTML, String>
{
  @Override
  public void initialize(CheckForHTML constraintAnnotation)
  {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext)
  {
    boolean isValidText = !s.matches("(.|\\n)*<((.|\\n)*)>.*?|<((.|\\n)*)>(.|\\n)*");
    return isValidText;
  }
}
