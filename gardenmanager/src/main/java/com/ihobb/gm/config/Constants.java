package com.ihobb.gm.config;

public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";
    public static final String CODE_REGEX = "^[A-Za-z0-9-]";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ANONYMOUS_USER = "anonymoususer";

    private Constants() throws IllegalAccessException {
        throw new IllegalAccessException("No instance available");
    }
}
