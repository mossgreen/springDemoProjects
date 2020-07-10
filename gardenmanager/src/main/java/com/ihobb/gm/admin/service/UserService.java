package com.ihobb.gm.admin.service;

import com.ihobb.gm.admin.domain.Organization;
import com.ihobb.gm.auth.domain.User;

public interface UserService {
    User fetchUserByEmail(String email);
    User fetchUserByName(String username);
}
