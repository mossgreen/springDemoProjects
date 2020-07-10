package com.ihobb.gm.config;

import com.ihobb.gm.admin.service.UserService;
import com.ihobb.gm.admin.service.UserServiceImpl;
import com.ihobb.gm.security.JwtUserDetailsService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

//@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
//    private final UserService userService;
    private JwtUserDetailsService jwtUserDetailsService;

    public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler, JwtUserDetailsService jwtUserDetailsService) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .successHandler(authenticationSuccessHandler)
            .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance()); // todo replace encoder
    }
}
