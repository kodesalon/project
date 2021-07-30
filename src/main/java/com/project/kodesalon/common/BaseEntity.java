package com.project.kodesalon.common;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "created_date_time", nullable = false)
    protected LocalDateTime createdDateTime;

    @Column(name = "last_modified_date_time")
    protected LocalDateTime lastModifiedDateTime;

    @Column(name = "deleted_date_time")
    protected LocalDateTime deletedDateTime;
}
