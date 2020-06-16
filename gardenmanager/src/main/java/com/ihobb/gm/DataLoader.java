package com.ihobb.gm;

import com.ihobb.gm.admin.domain.Authority;
import com.ihobb.gm.admin.domain.Organization;
import com.ihobb.gm.admin.domain.User;
import com.ihobb.gm.admin.repository.AuthorityRepository;
import com.ihobb.gm.admin.repository.OrganizationRepository;
import com.ihobb.gm.admin.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

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
            .description("org one ")
            .uuid(UUID.randomUUID())
            .createdBy(0L)
            .createdDate(Instant.now())
            .users(null)
            .build();

        final Organization build2 = Organization.builder()
            .name("orgTwo")
            .description("org Two ")
            .uuid(UUID.randomUUID())
            .createdBy(0L)
            .createdDate(Instant.now())
            .users(null)
            .build();

        final Organization build3 = Organization.builder()
            .name("orgThree")
            .description("org Three ")
            .uuid(UUID.randomUUID())
            .createdBy(0L)
            .createdDate(Instant.now())
            .users(null)
            .build();

        organizationRepository.save(build1);
        organizationRepository.save(build2);
        organizationRepository.save(build3);

        String password = UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).substring(0, 60);

        final User user1 = User.builder()
            .activated(Boolean.TRUE)
            .createdBy(0L)
            .uuid(UUID.randomUUID())
            .imageUrl("moss.io/url.jpg")
            .createdDate(Instant.now())
            .activationKey(UUID.randomUUID().toString().substring(0,20))
            .description("user one")
            .email("one@one.org")
            .firstName("firstOne")
            .lastName("lastOne")
            .langKey("EN")
            .login("OneOne")
            .password(password)
            .organizations(Set.of(build1, build2,build3))
            .authorities(Set.of(admin))
            .build();

        final User user2 = User.builder()
            .activated(Boolean.TRUE)
            .createdBy(0L)
            .uuid(UUID.randomUUID())
            .imageUrl("moss.io/url2.jpg")
            .createdDate(Instant.now())
            .activationKey(UUID.randomUUID().toString().substring(0,20))
            .description("user Two")
            .email("two@two.net")
            .firstName("firstTwo")
            .lastName("lastTwo")
            .login("Tutu")
            .langKey("CH")
            .password(password)
            .organizations(Set.of(build2))
            .authorities(Set.of(client))
            .build();

        final User user3 = User.builder()
            .activated(Boolean.TRUE)
            .createdBy(0L)
            .uuid(UUID.randomUUID())
            .imageUrl("moss.io/url3.jpg")
            .createdDate(Instant.now())
            .activationKey(UUID.randomUUID().toString().substring(0, 20))
            .description("user three")
            .email("three@three.com")
            .firstName("firstThree")
            .lastName("lastThree")
            .login("ThreePerson")
            .langKey("EN")
            .password(password)
            .organizations(Set.of(build3))
            .authorities(Set.of(guest))
            .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }
}
