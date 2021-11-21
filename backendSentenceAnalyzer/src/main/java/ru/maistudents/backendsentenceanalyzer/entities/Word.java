package ru.maistudents.backendsentenceanalyzer.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "words")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name = "defaultSeq", sequenceName = "WORD_SEQ", allocationSize = 1)
public class Word extends BaseEntity implements Serializable {

    @Column(name = "position", nullable = false)
    private Integer position;

    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_parent_text")
    private Text parentText;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_role")
    private Role role;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Text getParentText() {
        return parentText;
    }

    public void setParentText(Text parentText) {
        this.parentText = parentText;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
