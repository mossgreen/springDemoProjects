package com.ihobb.gm.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = DBContextHolder.getCurrentDb();
        if (tenant == null || tenant.length() < 1) {
            return DBContextHolder.DEFAULT_TENANT_ID;
        } else {
            return tenant;
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
}
