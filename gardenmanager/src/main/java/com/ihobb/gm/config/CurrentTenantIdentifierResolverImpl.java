package com.ihobb.gm.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String currentTenant = DBContextHolder.getCurrentDb();
        if (currentTenant == null || currentTenant.length() < 1) {
            return DBContextHolder.DEFAULT_TENANT_ID;
        } else {
            return currentTenant;
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
}
