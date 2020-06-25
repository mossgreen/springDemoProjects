package com.ihobb.gm.config;

import com.ihobb.gm.admin.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class DataSourceInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = (User) principal;

        if (user.getCurrentOrgCode() == null) {
            TenantAwareRoutingDataSource ds = new TenantAwareRoutingDataSource();
            ds.initDatasource();
        } else {
            //set data source here?
            final String orgCode = user.getCurrentOrgCode();
            TenantAwareRoutingDataSource ds = new TenantAwareRoutingDataSource(orgCode);
            TenantContextHolder.setDataSourceContext(ds);
        }

        return super.preHandle(request, response, handler);
    }
}
