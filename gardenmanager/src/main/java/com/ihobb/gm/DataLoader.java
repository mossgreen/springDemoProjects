package com.ihobb.gm;

import com.ihobb.gm.auth.domain.Authority;
import com.ihobb.gm.admin.domain.Organization;
import com.ihobb.gm.auth.domain.User;
import com.ihobb.gm.admin.repository.AuthorityRepository;
import com.ihobb.gm.admin.repository.OrganizationRepository;
import com.ihobb.gm.admin.repository.UserRepository;
import com.ihobb.gm.config.MultiTenantConnectionProviderImpl;
import com.ihobb.gm.utility.Utility;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Data
@Component
public class DataLoader implements ApplicationRunner {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public DataLoader(OrganizationRepository organizationRepository, UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        final String password = "c66f5610-ba10-465d-8104-3f08e938daca6881090f-e9c1-4e0e-b79a-";

        final Authority admin = Authority.builder()
            .name("ADMIN")
            .build();

        final Authority client = Authority.builder()
            .name("CLIENT")
            .build();

        final Authority guest = Authority.builder()
            .name("GUEST")
            .build();

        authorityRepository.save(admin);
        authorityRepository.save(client);
        authorityRepository.save(guest);

        final Organization build1 = Organization.builder()
            .name("orgOne")
            .code("ihobdb01")
            .description("org one ")
            .uuid(UUID.randomUUID())
            .viewId(Utility.getRandomNumberString())
            .createdBy(0L)
            .users(null)
            .build();

        final Organization build2 = Organization.builder()
            .name("orgTwo")
            .code("ihobdb02")
            .description("org Two ")
            .viewId(Utility.getRandomNumberString())
            .uuid(UUID.randomUUID())
            .createdBy(0L)
            .users(null)
            .build();

        final Organization build3 = Organization.builder()
            .name("orgThree")
            .code("ihobdb03")
            .description("org Three ")
            .uuid(UUID.randomUUID())
            .viewId(Utility.getRandomNumberString())
            .createdBy(0L)
            .users(null)
            .build();

        organizationRepository.save(build1);
        organizationRepository.save(build2);
        organizationRepository.save(build3);

//        String password = UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).substring(0, 60);

        final User user1 = User.builder()
            .activated(Boolean.TRUE)
            .createdBy(0L)
            .imageUrl("moss.io/url.jpg")
            .activationKey(UUID.randomUUID().toString().substring(0, 20))
            .description("user one")
            .viewId(Utility.getRandomNumberString())
            .email("one@one.org")
            .firstName("firstOne")
            .lastName("lastOne")
            .langKey("EN")
            .name("OneOne")
            .password(password)
            .currentOrgCode("ihobdb03")
            .organizations(Set.of(build1, build2, build3))
            .authorities(Set.of(admin))
            .build();

        final User user2 = User.builder()
            .activated(Boolean.TRUE)
            .createdBy(0L)
            .imageUrl("moss.io/url2.jpg")
            .activationKey(UUID.randomUUID().toString().substring(0,20))
            .description("user Two")
            .email("two@two.net")
            .firstName("firstTwo")
            .viewId(Utility.getRandomNumberString())
            .lastName("lastTwo")
            .name("Tutu")
            .langKey("CH")
            .password(password)
            .currentOrgCode("ihobdb03")
            .organizations(Set.of(build2))
            .authorities(Set.of(client))
            .build();

        final User user3 = User.builder()
            .activated(Boolean.TRUE)
            .createdBy(0L)
            .imageUrl("moss.io/url3.jpg")
            .activationKey(UUID.randomUUID().toString().substring(0, 20))
            .description("user three")
            .email("three@three.com")
            .firstName("firstThree")
            .lastName("lastThree")
            .name("ThreePerson")
            .langKey("EN")
            .password(password)
            .currentOrgCode("ihobdb03")
            .viewId(Utility.getRandomNumberString())
            .organizations(Set.of(build3))
            .authorities(Set.of(guest))
            .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        // ---


    }
}
