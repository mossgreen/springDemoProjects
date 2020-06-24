package com.ihobb.gm.config;

import com.ihobb.gm.admin.domain.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Configuration
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl = "/garden"; // todo hard code for now

        /**
         * todo
         * may be, it's better to redirect to /garden, only if your have the client role
         * if you don't have a proper role, should throw IllegalStateException sth
         */

        final boolean isAdmin = authorities.stream().anyMatch(a-> a.getAuthority().equalsIgnoreCase("ADMIN"));

        if (isAdmin) {
            redirectUrl = "/admin";
        } else {
            // switch datasource to the client database and get all gardens it has
            // create datasource for current client,

            final User user = (User) authentication.getPrincipal();

            final String orgCode = user.getCurrentOrgCode();
            TenantAwareRoutingDataSource ds = new TenantAwareRoutingDataSource(orgCode);
            DynamicDataSourceContextHolder.setDataSourceContext(ds);
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
