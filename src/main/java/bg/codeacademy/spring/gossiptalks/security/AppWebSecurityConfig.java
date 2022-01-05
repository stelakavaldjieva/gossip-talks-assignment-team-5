package bg.codeacademy.spring.gossiptalks.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
@Configuration
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter
{

  @Override
  protected void configure(HttpSecurity http) throws Exception
  {
    http.authorizeRequests()
        .antMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
        .antMatchers("/api/v1/**").authenticated()
        .and()
        .httpBasic()
        .and()
        .csrf().disable()
        .headers().frameOptions().disable();
  }

  @Bean
  public PasswordEncoder getEncoder()
  {
    return new BCryptPasswordEncoder();
  }

}
