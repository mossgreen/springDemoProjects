package com.ihobb.gm.admin.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ihobb.gm.baseEntity.AbstractAuditingEntity;
import com.ihobb.gm.config.Constants;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SuperBuilder
@EqualsAndHashCode(callSuper = true,exclude = {"authorities","organizations"})
@ToString(exclude = {"authorities","organizations"})
@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@NamedEntityGraph(
    name = "user-authorities-organizations-graph",
    attributeNodes = {@NamedAttributeNode("authorities"),@NamedAttributeNode("organizations")}
)
public class User extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(name = "user_name", length = 50, unique = true, nullable = false)
    private String name;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "user_password_hash", length = 60, nullable = false)
    private String password;

    @Size(max = 50)
    @Column(name = "user_first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "user_last_name", length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Column(name = "user_email", length = 254, unique = true)
    private String email;

    @NotNull
    @Column(name = "user_activated", nullable = false)
    private Boolean activated = false;

    @Size(min = 2, max = 10)
    @Column(name = "user_lang_key", length = 10)
    private String langKey;

    @Size(max = 256)
    @Column(name = "user_image_url", length = 256)
    private String imageUrl;

    @Size(max = 20)
    @Column(name = "user_activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 1000)
    @Column(name = "user_description")
    private String description;

    @Pattern(regexp = Constants.CODE_REGEX)
    @Size(min = 8, max = 8)
    @Column(name = "user_current_org_code", nullable = true)
    private String currentOrgCode;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "user_authority",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "user_organization",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "or_id", referencedColumnName = "org_id")})
    @BatchSize(size = 20)
    private Set<Organization> organizations = new HashSet<>();

}
