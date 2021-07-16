package com.project.kodesalon.common;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "created_date")
    private LocalDateTime createdDateTime;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDateTime;

    @Column(name = "deleted_date")
    protected LocalDateTime deletedDate;
}
