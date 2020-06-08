package com.mossj.springsecurity;

import java.sql.Blob;

public class SecurityConstants {

    public static final String AUTH_LOGIN_URL = "/api/token";
    public static final String JWT_SECRET = "pRNsm7m8JoGl0q9uY8F/YquCcCA4rFPzjZqtMdzrqPk=";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";

    private SecurityConstants() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }
}

