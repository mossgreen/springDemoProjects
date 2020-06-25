package com.ihobb.gm.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

/**
 * Thread shared context to point to the datasource which should be used. This
 * enables context switches between different clients.
 */
public class TenantContextHolder {

    public static final String DEFAULT_TENANT = "admin"; // admin database

    private static final ThreadLocal<String> TENANT_CONTEXT = new ThreadLocal<>();

    public static void setTenant(String tenantIdentifier) {
        TENANT_CONTEXT.set(tenantIdentifier);
    }

    public static void reset(String tenantIdentifier) {
        TENANT_CONTEXT.remove();
    }

    public static class TenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

        @Override
        public String resolveCurrentTenantIdentifier() {
            String currentTenantId = TENANT_CONTEXT.get();
            return currentTenantId != null ? currentTenantId : TENANT_CONTEXT.get();
        }

        @Override
        public boolean validateExistingCurrentSessions() {
            return false;
        }
    }
}
