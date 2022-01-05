package bg.codeacademy.spring.gossiptalks.validation;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintValidatorContext;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@DataJpaTest
@ContextConfiguration(locations = {"classpath:spring-test-config.xml"})
public class StrongPasswordValidatorTest
{
  private final StrongPasswordValidator strongPasswordValidator = new StrongPasswordValidator();

  @Mock
  private ConstraintValidatorContext cxt;

  @BeforeMethod
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);
    Mockito.when(cxt.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));
  }

  @Test
  public void given_null_When_validate_Then_false()
  {
    assertFalse(strongPasswordValidator.isValid(null, cxt));
    Mockito.verify(cxt, Mockito.times(1)).buildConstraintViolationWithTemplate(Mockito.anyString());

  }

  @Test
  public void given_valid_password_When_validating_Then_true()
  {
    assertTrue(strongPasswordValidator.isValid("gosho123!G", cxt));
    Mockito.verify(cxt, Mockito.times(0)).buildConstraintViolationWithTemplate(Mockito.anyString());
  }

  @Test
  public void given_short_password_When_validating_Then_false()
  {
    assertFalse(strongPasswordValidator.isValid("123", cxt));
    assertFalse(strongPasswordValidator.isValid("", cxt));
    assertFalse(strongPasswordValidator.isValid("1234675757", cxt));
    Mockito.verify(cxt, Mockito.times(3)).buildConstraintViolationWithTemplate(Mockito.anyString());
  }
}