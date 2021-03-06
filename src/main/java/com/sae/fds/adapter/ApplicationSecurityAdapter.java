package com.sae.fds.adapter;

import com.sae.fds.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/* Hier wird die secruity definiert, welcher Rolle darf auf was zugreifen*/

@Configuration
@Order(SecurityProperties.DEFAULT_FILTER_ORDER)
public class ApplicationSecurityAdapter extends WebSecurityConfigurerAdapter {
    // @Autowired
    // private SecurityProperties security;

    @Autowired
    private AccountService accountService;

    @Value("${app.key}")
    private String applicationSecret;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/forgotpassword").permitAll().and().authorizeRequests().antMatchers("/account/autologin").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/account/delete").access("hasRole('ROLE_ADMIN')").antMatchers("/account/register")
                .access("hasRole('ROLE_ADMIN')").antMatchers("/account/list").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/account/list/**").access("hasRole('ROLE_ADMIN')").antMatchers("/organization")
                .access("hasRole('ROLE_ADMIN')").antMatchers("/organization/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/unit").access("hasRole('ROLE_ADMIN')").antMatchers("/unit/**")
                .access("hasRole('ROLE_ADMIN')").antMatchers("/room").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/room/**").access("hasRole('ROLE_ADMIN')").antMatchers("/event/list/all")
                .access("hasRole('ROLE_ADMIN')").antMatchers("/assets/**").permitAll().antMatchers("/js/**").permitAll()
                .antMatchers("/css/**").permitAll().antMatchers("/calendar/room").permitAll()
                .antMatchers("/calendar/room/**").permitAll().antMatchers("/calendar/events").permitAll().anyRequest()
                .authenticated().and().formLogin().loginPage("/welcome").failureUrl("/welcome?errorl").permitAll().and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/welcome").and()
                .rememberMe().key(applicationSecret).tokenValiditySeconds(31536000);

        http.csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
