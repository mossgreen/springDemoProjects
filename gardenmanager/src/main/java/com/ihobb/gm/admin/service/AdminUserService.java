package com.ihobb.gm.admin.service;

import com.ihobb.gm.admin.domain.Organization;
import com.ihobb.gm.auth.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AdminUserService extends UserDetailsService {
    User fetchUserByEmail(String email);
    Organization fetchOrganizationByCode(String orgCode);
}
