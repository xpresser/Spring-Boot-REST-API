package com.metodi.projectapp.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter @NoArgsConstructor
public abstract class BaseData {

    @CreationTimestamp
    @Column(name = "date_created", nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @UpdateTimestamp
    @Column(name = "date_edited")
    private LocalDateTime dateEdited;

    @Column(name = "creator_id", updatable = false)
    private long creatorID;

    @Column(name = "editor_id")
    private long editorID;

    @PrePersist
    protected void onCreate() {
        this.dateCreated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dateEdited = LocalDateTime.now();
    }
}
