package com.ihobb.gm.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ihobb.gm.auth.domain.User;
import com.ihobb.gm.baseEntity.AbstractAuditingEntity;
import com.ihobb.gm.constant.Constants;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@SuperBuilder
@EqualsAndHashCode(callSuper = true, exclude = {"users"})
@Entity
@Data
@Table(name = "organization")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"users"})
public class Organization  extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "org_id")
    private Long id;

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(name = "org_name", length = 50, unique = true, nullable = false)
    private String name;

    @Pattern(regexp = Constants.CODE_REGEX)
    @Size(min = 8, max = 8)
    @Column(name = "org_code", length = 8, unique = true, nullable = false)
    private String code;

    @Size(max = 1000)
    @Column(name = "org_description")
    private String description;

    @ManyToMany(mappedBy = "organizations")
    private Set<User> users = new HashSet<>();
}
