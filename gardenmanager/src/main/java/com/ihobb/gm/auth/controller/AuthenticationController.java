package com.ihobb.gm.auth.controller;

import com.ihobb.gm.admin.service.UserService;
import com.ihobb.gm.auth.domain.User;
import com.ihobb.gm.config.DBContextHolder;
import com.ihobb.gm.dto.AuthResponse;
import com.ihobb.gm.dto.UserLoginDTO;
import com.ihobb.gm.utility.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/auth/")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("login")
    public AuthResponse userLogin(@RequestBody @NotNull UserLoginDTO userLoginDTO) throws AuthenticationException {

        if(null == userLoginDTO.getUserName() || userLoginDTO.getUserName().isEmpty()){
            throw new RuntimeException("please!");
        }

        final User user = userService.fetchUserByName(userLoginDTO.getUserName());

        final String currentOrgCode = user.getCurrentOrgCode();

//        DBContextHolder.setCurrentDb(currentOrgCode);

        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getUserName(), userLoginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateToken(userDetails.getUsername(),String.valueOf(currentOrgCode));

        return new AuthResponse(userDetails.getUsername(), token);
    }
}
