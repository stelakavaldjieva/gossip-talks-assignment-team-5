package bg.codeacademy.spring.gossiptalks.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String>
{
  @Override
  public void initialize(StrongPassword constraintAnnotation)
  {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext)
  {
    if (password == null) {
      constraintValidatorContext.buildConstraintViolationWithTemplate("Password should not be null!").addConstraintViolation();
      return false;
    }


    boolean containsUpperCaseLetter = false;
    boolean containsLowerCaseLetter = false;
    boolean containsDigit = false;
    boolean containsSpecialSymbol = false;
    boolean isWithinSizeLimits = password.length() > 7 && password.length() < 65;
    boolean containsWhiteSpace = false;

    for (Character c : password.toCharArray()) {
      if (Character.isUpperCase(c)) {
        containsUpperCaseLetter = true;
      }
      if (Character.isLowerCase(c)) {
        containsLowerCaseLetter = true;
      }
      if (Character.isDigit(c)) {
        containsDigit = true;
      }
      if (!Character.isAlphabetic(c) && !Character.isDigit(c) && !Character.isWhitespace(c)) {
        containsSpecialSymbol = true;
      }
      if (Character.isWhitespace(c)) {
        containsWhiteSpace = true;
      }
    }

    if (!containsUpperCaseLetter) {
      constraintValidatorContext.buildConstraintViolationWithTemplate("Password should contain uppercase letter!").addConstraintViolation();
    }
    else if (!containsLowerCaseLetter) {
      constraintValidatorContext.buildConstraintViolationWithTemplate("Password should contain lowercase letter!").addConstraintViolation();
    }
    else if (!containsDigit) {
      constraintValidatorContext.buildConstraintViolationWithTemplate("Password should contain a digit!").addConstraintViolation();
    }
    else if (!containsSpecialSymbol) {
      constraintValidatorContext.buildConstraintViolationWithTemplate("Password should contain a special symbol!").addConstraintViolation();
    }
    else if (!isWithinSizeLimits) {
      constraintValidatorContext.buildConstraintViolationWithTemplate("Password should be within size limits!").addConstraintViolation();
    }
    else if (containsWhiteSpace) {
      constraintValidatorContext.buildConstraintViolationWithTemplate("Password should not contain whitespaces!").addConstraintViolation();
    }

    return containsDigit &&
        containsLowerCaseLetter &&
        containsUpperCaseLetter &&
        containsSpecialSymbol &&
        isWithinSizeLimits &&
        (!containsWhiteSpace);
  }
}
