package com.ihobb.gm.admin.domain;

import com.ihobb.gm.admin.repository.OrganizationRepository;
import com.ihobb.gm.baseEntity.AbstractAuditingEntity;
import com.ihobb.gm.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SuperBuilder
@EqualsAndHashCode(callSuper = true, exclude = {"users"})
@Entity
@Table(name = "organization")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"users"})
public class Organization  extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "organization_id")
    private Long id;

    @org.hibernate.annotations.Type(type = "pg-uuid")
    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(name = "organization_name", length = 50, unique = true, nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(name = "organization_description")
    private String description;

    @ManyToMany(mappedBy = "organizations")
    private Set<User> users = new HashSet<>();
}
