package ru.maistudents.backendsentenceanalyzer.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name = "defaultSeq", sequenceName = "ROLE_SEQ", allocationSize = 1)
public class Role extends BaseEntity implements Serializable {

    @Column(name = "name", nullable = false)
    private String name;

    public Role(String name) {
        this.name = name;
    }

    public Role() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
