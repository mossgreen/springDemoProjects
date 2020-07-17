package com.ihobb.gm.security;

import com.ihobb.gm.admin.service.OrganizationService;
import com.ihobb.gm.admin.service.UserService;
import com.ihobb.gm.utility.JwtTokenUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;

@Log4j2
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private JwtUserDetailsService jwtUserDetailsService;
    @Resource
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

//    @Bean
//    public JwtAuthenticationFilter authenticationTokenFilterBean() {
//        return new JwtAuthenticationFilter(jwtUserDetailsService,jwtTokenUtil, userService, organizationService);
//    }

    @Bean
    public FilterRegistrationBean<CorsFilter> platformCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Accept");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("OPTIONS");
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);

        final CorsFilter corsFilter = new CorsFilter(source);
//        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(corsFilter);
        bean.setOrder(-110);
        return bean;
    }

    /**
     *  We do this to ensure our Filter is only loaded once into Application Context
     *
     */
//    @Bean(name = "authenticationFilterRegistration")
//    public FilterRegistrationBean<OncePerRequestFilter> JwtAuthenticationFilterRegistration() {
//        final FilterRegistrationBean<OncePerRequestFilter>  filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(authenticationTokenFilterBean());
//        filterRegistrationBean.setEnabled(false);
//        return filterRegistrationBean;
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
            .csrf().disable()
            .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class )
            .logout()
                .logoutUrl("/logout")
//                .addLogoutHandler()// todo
                .and()
            .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/admin/**").authenticated()
                .antMatchers("/garden/**").authenticated()
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
