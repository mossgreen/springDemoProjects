package com.ihobb.gm.config;

import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * Thread shared context to point to the datasource which should be used. This
 * enables context switches between different clients.
 */
public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<DataSource> DATA_SOURCE_CONTEXT = new ThreadLocal<>();

    public static void setDataSourceContext(DataSource dataSource) {
        Assert.notNull(dataSource, "tenant datasource is not set");
        DATA_SOURCE_CONTEXT.set(dataSource);
    }

    public static DataSource getDataSourceContext() {
        return DATA_SOURCE_CONTEXT.get();
    }

    public static void clear() {
        DATA_SOURCE_CONTEXT.remove();
    }
}
