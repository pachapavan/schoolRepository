package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TabelValues.
 */
@Entity
@Table(name = "tabel_values")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TabelValues implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private Attributes attributes;

    @OneToMany(mappedBy = "permissions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "json", "permissions", "permissions1", "json1", "json2" }, allowSetters = true)
    private Set<ObjectContainingString> objectContainingStrings = new HashSet<>();

    @OneToMany(mappedBy = "displayAtt")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "spacing", "attributes", "text", "badge", "icon", "image", "badgeTypes", "displayAtt" },
        allowSetters = true
    )
    private Set<DisplayAtt> displayAtts = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "attributes", "tabelValues" }, allowSetters = true)
    private Body tableValues;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TabelValues id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public TabelValues attributes(Attributes attributes) {
        this.setAttributes(attributes);
        return this;
    }

    public Set<ObjectContainingString> getObjectContainingStrings() {
        return this.objectContainingStrings;
    }

    public void setObjectContainingStrings(Set<ObjectContainingString> objectContainingStrings) {
        if (this.objectContainingStrings != null) {
            this.objectContainingStrings.forEach(i -> i.setPermissions(null));
        }
        if (objectContainingStrings != null) {
            objectContainingStrings.forEach(i -> i.setPermissions(this));
        }
        this.objectContainingStrings = objectContainingStrings;
    }

    public TabelValues objectContainingStrings(Set<ObjectContainingString> objectContainingStrings) {
        this.setObjectContainingStrings(objectContainingStrings);
        return this;
    }

    public TabelValues addObjectContainingString(ObjectContainingString objectContainingString) {
        this.objectContainingStrings.add(objectContainingString);
        objectContainingString.setPermissions(this);
        return this;
    }

    public TabelValues removeObjectContainingString(ObjectContainingString objectContainingString) {
        this.objectContainingStrings.remove(objectContainingString);
        objectContainingString.setPermissions(null);
        return this;
    }

    public Set<DisplayAtt> getDisplayAtts() {
        return this.displayAtts;
    }

    public void setDisplayAtts(Set<DisplayAtt> displayAtts) {
        if (this.displayAtts != null) {
            this.displayAtts.forEach(i -> i.setDisplayAtt(null));
        }
        if (displayAtts != null) {
            displayAtts.forEach(i -> i.setDisplayAtt(this));
        }
        this.displayAtts = displayAtts;
    }

    public TabelValues displayAtts(Set<DisplayAtt> displayAtts) {
        this.setDisplayAtts(displayAtts);
        return this;
    }

    public TabelValues addDisplayAtt(DisplayAtt displayAtt) {
        this.displayAtts.add(displayAtt);
        displayAtt.setDisplayAtt(this);
        return this;
    }

    public TabelValues removeDisplayAtt(DisplayAtt displayAtt) {
        this.displayAtts.remove(displayAtt);
        displayAtt.setDisplayAtt(null);
        return this;
    }

    public Body getTableValues() {
        return this.tableValues;
    }

    public void setTableValues(Body body) {
        this.tableValues = body;
    }

    public TabelValues tableValues(Body body) {
        this.setTableValues(body);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TabelValues)) {
            return false;
        }
        return id != null && id.equals(((TabelValues) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TabelValues{" +
            "id=" + getId() +
            "}";
    }
}
