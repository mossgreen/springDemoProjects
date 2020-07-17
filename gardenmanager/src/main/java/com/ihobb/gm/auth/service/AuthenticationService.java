package com.ihobb.gm.auth.service;

public interface AuthenticationService {

    public String refreshToken(String token);

    String login(String username, String password);
}
