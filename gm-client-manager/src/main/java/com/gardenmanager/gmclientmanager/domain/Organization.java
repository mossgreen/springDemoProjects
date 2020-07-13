package com.gardenmanager.gmclientmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.gardenmanager.gmclientmanager.Constants;
import com.gardenmanager.gmclientmanager.baseEntity.AbstractAuditingEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.io.Serializable;

@SuperBuilder
@Entity
@Data
@Table(name = "organization")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"users"})
public class Organization extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "org_id")
    private Long id;

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(name = "org_name", length = 100, unique = true, nullable = false)
    private String name;

    @Pattern(regexp = Constants.CODE_REGEX)
    @Size(min = 8, max = 8)
    @Column(name = "org_code", length = 8, unique = true, nullable = false)
    private String code;

    @Size(max = 1000)
    @Column(name = "org_description")
    private String description;

}
