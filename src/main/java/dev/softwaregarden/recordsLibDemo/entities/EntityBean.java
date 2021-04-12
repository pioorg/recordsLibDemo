package dev.softwaregarden.recordsLibDemo.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
public class EntityBean {

    @Id
    private Long id;

    public EntityBean(Long id) {
        this.id = id;
    }

    protected EntityBean() {
        //for JPA
    }

    public Long getId() {
        return id;
    }
}
