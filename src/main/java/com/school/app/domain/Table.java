package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Table.
 */
@Entity
@Table(name = "jhi_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Table implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "generic_object")
    private String genericObject;

    @OneToOne
    @JoinColumn(unique = true)
    private Attributes attributes;

    @JsonIgnoreProperties(value = { "attributes", "tabelValues" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Body body;

    @JsonIgnoreProperties(value = { "attributes", "labels" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Head head;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Table id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenericObject() {
        return this.genericObject;
    }

    public Table genericObject(String genericObject) {
        this.setGenericObject(genericObject);
        return this;
    }

    public void setGenericObject(String genericObject) {
        this.genericObject = genericObject;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Table attributes(Attributes attributes) {
        this.setAttributes(attributes);
        return this;
    }

    public Body getBody() {
        return this.body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Table body(Body body) {
        this.setBody(body);
        return this;
    }

    public Head getHead() {
        return this.head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Table head(Head head) {
        this.setHead(head);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Table)) {
            return false;
        }
        return id != null && id.equals(((Table) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Table{" +
            "id=" + getId() +
            ", genericObject='" + getGenericObject() + "'" +
            "}";
    }
}
