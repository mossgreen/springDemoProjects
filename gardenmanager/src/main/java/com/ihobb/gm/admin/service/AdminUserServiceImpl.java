package com.ihobb.gm.admin.service;

import com.ihobb.gm.admin.domain.Organization;
import com.ihobb.gm.auth.domain.User;
import com.ihobb.gm.admin.repository.OrganizationRepository;
import com.ihobb.gm.admin.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService, UserDetailsService {

    private final UserRepository userRepository;
    private final OrganizationRepository orgRepository;

    public AdminUserServiceImpl(UserRepository userRepository, OrganizationRepository orgRepository) {
        this.userRepository = userRepository;
        this.orgRepository = orgRepository;
    }

    public User fetchUserByEmail(String email) {
        return userRepository.findAllByEmailAndActivatedIsTrue(email).get(0);
    }

    @Override
    public Organization fetchOrganizationByCode(String orgCode) {
        return orgRepository.findByCode(orgCode).orElseThrow(RuntimeException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final List<User> users = userRepository.findAllByEmailAndActivatedIsTrue(username);
        if (users.isEmpty()) {
            throw new RuntimeException("Username or password are incorrect.");
        }
        return userRepository.findAllByEmailAndActivatedIsTrue(username).get(0);
    }
}
