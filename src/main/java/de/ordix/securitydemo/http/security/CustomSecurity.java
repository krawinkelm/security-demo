package de.ordix.securitydemo.http.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity(debug = false)
public class CustomSecurity {

    @Configuration
    @Order(1)
    public static class PermitAllConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/without-authentication").authorizeRequests().antMatchers("/without-authentication").permitAll();
        }
    }

    @Configuration
    @Order(2)
    public static class BasicAuthenticationConfiguration extends WebSecurityConfigurerAdapter {

        private static final String ROLE = "DEMO";

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/basic-authentication")
                    .authorizeRequests()
                    .antMatchers("/basic-authentication").hasRole(ROLE)
                    .and()
                    .httpBasic();
            ;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth)
                throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("demo")
                    .password("{noop}demo123")
                    .roles(ROLE);
        }
    }

    @Configuration
    public static class EverythingElseConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.anonymous().disable().addFilterAfter(new CustomAuthenticationFilter(),
                    UsernamePasswordAuthenticationFilter.class);
        }
    }

}
