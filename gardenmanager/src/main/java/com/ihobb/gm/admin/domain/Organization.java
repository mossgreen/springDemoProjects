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
@Data
@Table(name = "organization")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"users"})
public class Organization  extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
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
