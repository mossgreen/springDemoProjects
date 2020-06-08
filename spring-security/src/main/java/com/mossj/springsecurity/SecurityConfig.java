package com.mossj.springsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private final CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider;
//
//    public SecurityConfig(CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider) {
//        this.customUsernamePasswordAuthenticationProvider = customUsernamePasswordAuthenticationProvider;
//    }



    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/guest").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic()
            .and()
            .addFilter(new JwtAuthenticateFilter(authenticationManager()))
            .addFilter(new JwtAuthorizationFilter(authenticationManager()))
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    //    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//            .authorizeRequests()
//            .anyRequest()
//            .authenticated()
//            .and()
//            .httpBasic()
//            .and()
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//            .authorizeRequests()
//            .anyRequest()
//            .authenticated()
//            .and()
//            .formLogin()
//            .successForwardUrl("/home")
//            .and()
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//            .authorizeRequests()
//            .anyRequest()
//            .authenticated()
//            .and()
//            .httpBasic()
//            .and()
//            .formLogin()
//            .defaultSuccessUrl("/index");
//    }



//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().ignoringAntMatchers("/h2/**")
//            .and()
//            .headers().frameOptions().sameOrigin()
//            .and()
//            .authorizeRequests()
//            .antMatchers("/h2/**").permitAll()
//            .anyRequest()
//            .authenticated()
//            .and()
//            .formLogin();
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth)  throws  Exception {
//        auth.eraseCredentials(false);
//        auth.authenticationProvider(customUsernamePasswordAuthenticationProvider);
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}

