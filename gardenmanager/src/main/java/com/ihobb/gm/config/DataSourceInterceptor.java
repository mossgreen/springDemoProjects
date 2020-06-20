package com.ihobb.gm.config;

import com.ihobb.gm.admin.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.logging.Handler;

@Component
public class DataSourceInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /**
         * if it's from login url, we should:
         * 1. there is a user in context,
         *      - and it's admin, lead it to admin page
         *      - otherwise, take it to it's org data source
         * 2. there is no user in context,
         *      - throw exception?
         */

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = (User) principal;

        if (user.getCurrentOrgId() != null) {
            //set data source here?

            DynamicTenantAwareRoutingDataSource ds = new DynamicTenantAwareRoutingDataSource();

            final DataSource dataSource = ds.determineTargetDataSource();
            TenantDataSourceContextHolder.setDataSourceContext(dataSource);
        }

        return super.preHandle(request, response, handler);
    }
}
