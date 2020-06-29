package com.ihobb.gm.config;

/**
 * Thread shared context to point to the datasource which should be used. This
 * enables context switches between different clients.
 */
public class DBContextHolder {

    public static final String DEFAULT_TENANT_ID = "admin";

    private static final ThreadLocal<String> TENANT_CONTEXT = new ThreadLocal<>();

    public static void setCurrentDb(String dbType) {
        TENANT_CONTEXT.set(dbType);
    }

    public static String getCurrentDb() {
        return TENANT_CONTEXT.get();
    }

    public static void clear() {
        TENANT_CONTEXT.remove();
    }
}
