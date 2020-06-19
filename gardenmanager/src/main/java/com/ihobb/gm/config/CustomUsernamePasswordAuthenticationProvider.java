package com.ihobb.gm.config;

import com.ihobb.gm.admin.domain.Organization;
import com.ihobb.gm.admin.domain.User;
import com.ihobb.gm.admin.repository.OrganizationRepository;
import com.ihobb.gm.admin.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

@Component
public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    public CustomUsernamePasswordAuthenticationProvider(UserRepository userRepository, OrganizationRepository organizationRepository) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userEmail = authentication.getName();
        String password = authentication.getCredentials().toString();

        final List<User> users = userRepository.findAllByEmailAndActivatedIsTrue(userEmail);

        if (users.isEmpty()) {
            throw new RuntimeException("failed authentication");
        } else {
            final User theUser = users.stream().filter(u -> u.getEmail().equals(userEmail)).findAny().orElseThrow(RuntimeException::new);

//            boolean isPasswordCorrect = BCrypt.checkpw(password, passwordDB);

            if (theUser.getPassword().equals(password)) {

                boolean admin = theUser.getAuthorities().stream().anyMatch(a -> a.getName().equals("ADMIN"));

                if (admin) {
                    List<Organization> all = organizationRepository.findAll();
                    // should show org page
                } else {
                    @Pattern(regexp = Constants.LOGIN_REGEX) @Size(min = 8, max = 8) String code = theUser.getOrganizations().stream().filter(org -> org.getId().equals(theUser.getCurrentOrgId()))
                        .findAny().get().getCode();

                    // create a new datasource?

                }

                return new UsernamePasswordAuthenticationToken(userEmail, password, theUser.getAuthorities());
            } else {
                throw new RuntimeException("failed authentication");
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
