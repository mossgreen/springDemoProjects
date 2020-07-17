package com.ihobb.gm.auth.controller;

import com.ihobb.gm.auth.service.AuthenticationService;
import com.ihobb.gm.constant.JWTConstants;
import com.ihobb.gm.dto.AuthResponse;
import com.ihobb.gm.dto.UserLoginDTO;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public AuthResponse userLogin(@RequestBody @NotNull UserLoginDTO userLoginDTO) throws AuthenticationException {

        if (null == userLoginDTO.getUserName() || userLoginDTO.getUserName().isEmpty()) {
            throw new RuntimeException("please!");
        }

        final String token = authenticationService.login(userLoginDTO.getUserName(), userLoginDTO.getPassword());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new AuthResponse(userDetails.getUsername(), token);
    }

    @RequestMapping(value = "/refreshtoken")
    public String refresh(@RequestHeader(JWTConstants.TOKEN_PREFIX) String token) {
        return authenticationService.refreshToken(token);
    }
}
