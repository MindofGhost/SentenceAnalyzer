package ru.maistudents.backendsentenceanalyzer.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "texts")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name = "defaultSeq", sequenceName = "TEXT_SEQ", allocationSize = 1)
public class Text extends BaseEntity implements Serializable {

    @Lob
    @Column(name = "text_value", nullable = false, length = 65535)
    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
