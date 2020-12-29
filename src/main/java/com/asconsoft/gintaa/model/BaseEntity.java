package com.asconsoft.gintaa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = -4358206709011769504L;

    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator",
            strategy = "com.asconsoft.gintaa.sequence.IdGenerator")
    private String id;

    @JsonIgnore
    @CreatedBy
    protected String createdBy;

    @JsonIgnore
    @CreatedDate
    protected LocalDateTime createdDate;

    @JsonIgnore
    @LastModifiedBy
    protected String lastModifiedBy;

    @JsonIgnore
    @LastModifiedDate
    protected LocalDateTime lastModifiedDate;
}