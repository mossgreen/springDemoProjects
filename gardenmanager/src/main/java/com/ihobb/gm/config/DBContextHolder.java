package com.ihobb.gm.config;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Thread shared context to point to the datasource which should be used. This
 * enables context switches between different clients.
 */
public class DBContextHolder {

    public static final ThreadLocal<String> TENANT_CONTEXT = new ThreadLocal<>();

    public static void setCurrentDb(String dbName) {

        Assert.notNull(dbName, "database name cannot be null");
        TENANT_CONTEXT.set(dbName);
    }

    public static String getCurrentDb() {
        return TENANT_CONTEXT.get();
    }

    public static void clear() {
        TENANT_CONTEXT.remove();
    }
}
