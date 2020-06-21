package com.ihobb.gm.config;

import com.ihobb.gm.admin.domain.User;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.logging.Handler;

@Component
public class DataSourceInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = (User) principal;

        if (user.getCurrentOrgCode() == null) {
            DynamicTenantAwareRoutingDataSource ds = new DynamicTenantAwareRoutingDataSource();
            ds.initDatasource();
        } else {
            //set data source here?
            final String orgCode = user.getCurrentOrgCode();
            DynamicTenantAwareRoutingDataSource ds = new DynamicTenantAwareRoutingDataSource(orgCode);
            TenantDataSourceContextHolder.setDataSourceContext(ds);
        }

        return super.preHandle(request, response, handler);
    }
}
