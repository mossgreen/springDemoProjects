package com.gardenmanager.gmclientmanager.baseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gardenmanager.gmclientmanager.utility.Utility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 6, min = 6)
    @Column(name = "view_id", nullable = false, updatable = false, unique = true)
    private String viewId;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    @JsonIgnore
    private Long createdBy;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    @JsonIgnore
    private Instant createdDate = Instant.now();

    @LastModifiedBy
    @Column(name = "last_modified_by")
    @JsonIgnore
    private Long lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @JsonIgnore
    private Instant lastModifiedDate = Instant.now();

    @org.hibernate.annotations.Type(type = "pg-uuid")
    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @PrePersist
    void onCreate() {
        this.setCreatedDate(Instant.now());
        this.setUuid(UUID.randomUUID());
        this.setViewId(Utility.getRandomNumberString());
    }

    @PreUpdate
    void onUpdate() {
        this.setLastModifiedDate(Instant.now());
    }
}
