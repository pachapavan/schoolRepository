package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Head.
 */
@Entity
@Table(name = "head")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Head implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private Attributes attributes;

    @OneToMany(mappedBy = "labels")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "displayAtt", "spacing", "text", "icon", "objectContainingStrings", "labels" }, allowSetters = true)
    private Set<Label> labels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Head id(Long id) {
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

    public Head attributes(Attributes attributes) {
        this.setAttributes(attributes);
        return this;
    }

    public Set<Label> getLabels() {
        return this.labels;
    }

    public void setLabels(Set<Label> labels) {
        if (this.labels != null) {
            this.labels.forEach(i -> i.setLabels(null));
        }
        if (labels != null) {
            labels.forEach(i -> i.setLabels(this));
        }
        this.labels = labels;
    }

    public Head labels(Set<Label> labels) {
        this.setLabels(labels);
        return this;
    }

    public Head addLabel(Label label) {
        this.labels.add(label);
        label.setLabels(this);
        return this;
    }

    public Head removeLabel(Label label) {
        this.labels.remove(label);
        label.setLabels(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Head)) {
            return false;
        }
        return id != null && id.equals(((Head) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Head{" +
            "id=" + getId() +
            "}";
    }
}
