package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Spacing.
 */
@Entity
@Table(name = "spacing")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Spacing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "class_name")
    private String className;

    @OneToMany(mappedBy = "json1")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "json", "permissions", "permissions1", "json1", "json2" }, allowSetters = true)
    private Set<ObjectContainingString> margins = new HashSet<>();

    @OneToMany(mappedBy = "json2")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "json", "permissions", "permissions1", "json1", "json2" }, allowSetters = true)
    private Set<ObjectContainingString> paddings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Spacing id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return this.className;
    }

    public Spacing className(String className) {
        this.setClassName(className);
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Set<ObjectContainingString> getMargins() {
        return this.margins;
    }

    public void setMargins(Set<ObjectContainingString> objectContainingStrings) {
        if (this.margins != null) {
            this.margins.forEach(i -> i.setJson1(null));
        }
        if (objectContainingStrings != null) {
            objectContainingStrings.forEach(i -> i.setJson1(this));
        }
        this.margins = objectContainingStrings;
    }

    public Spacing margins(Set<ObjectContainingString> objectContainingStrings) {
        this.setMargins(objectContainingStrings);
        return this;
    }

    public Spacing addMargin(ObjectContainingString objectContainingString) {
        this.margins.add(objectContainingString);
        objectContainingString.setJson1(this);
        return this;
    }

    public Spacing removeMargin(ObjectContainingString objectContainingString) {
        this.margins.remove(objectContainingString);
        objectContainingString.setJson1(null);
        return this;
    }

    public Set<ObjectContainingString> getPaddings() {
        return this.paddings;
    }

    public void setPaddings(Set<ObjectContainingString> objectContainingStrings) {
        if (this.paddings != null) {
            this.paddings.forEach(i -> i.setJson2(null));
        }
        if (objectContainingStrings != null) {
            objectContainingStrings.forEach(i -> i.setJson2(this));
        }
        this.paddings = objectContainingStrings;
    }

    public Spacing paddings(Set<ObjectContainingString> objectContainingStrings) {
        this.setPaddings(objectContainingStrings);
        return this;
    }

    public Spacing addPadding(ObjectContainingString objectContainingString) {
        this.paddings.add(objectContainingString);
        objectContainingString.setJson2(this);
        return this;
    }

    public Spacing removePadding(ObjectContainingString objectContainingString) {
        this.paddings.remove(objectContainingString);
        objectContainingString.setJson2(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Spacing)) {
            return false;
        }
        return id != null && id.equals(((Spacing) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Spacing{" +
            "id=" + getId() +
            ", className='" + getClassName() + "'" +
            "}";
    }
}
