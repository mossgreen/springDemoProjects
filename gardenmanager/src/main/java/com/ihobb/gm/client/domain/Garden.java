package com.ihobb.gm.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ihobb.gm.baseEntity.AbstractAuditingEntity;
import com.ihobb.gm.constant.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@Entity
@Table(name = "garden")
@NoArgsConstructor
public class Garden extends AbstractAuditingEntity implements Serializable {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "garden_id")
    private Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(name = "garden_name", length = 50, unique = true, nullable = false)
    private String name;

    @Size(max = 256)
    @Column(name = "garden_image_url", length = 256)
    private String imageUrl;

    @Size(max = 1000)
    @Column(name = "garden_description")
    private String description;
}
