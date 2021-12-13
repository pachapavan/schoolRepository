package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ObjectContainingString.
 */
@Entity
@Table(name = "object_containing_string")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ObjectContainingString implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JsonIgnoreProperties(value = { "genericObjectsLists", "flexBoxes" }, allowSetters = true)
    private Page json;

    @ManyToOne
    @JsonIgnoreProperties(value = { "attributes", "objectContainingStrings", "displayAtts", "tableValues" }, allowSetters = true)
    private TabelValues permissions;

    @ManyToOne
    @JsonIgnoreProperties(value = { "displayAtt", "spacing", "text", "icon", "objectContainingStrings", "labels" }, allowSetters = true)
    private Label permissions1;

    @ManyToOne
    @JsonIgnoreProperties(value = { "margins", "paddings" }, allowSetters = true)
    private Spacing json1;

    @ManyToOne
    @JsonIgnoreProperties(value = { "margins", "paddings" }, allowSetters = true)
    private Spacing json2;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ObjectContainingString id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ObjectContainingString name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Page getJson() {
        return this.json;
    }

    public void setJson(Page page) {
        this.json = page;
    }

    public ObjectContainingString json(Page page) {
        this.setJson(page);
        return this;
    }

    public TabelValues getPermissions() {
        return this.permissions;
    }

    public void setPermissions(TabelValues tabelValues) {
        this.permissions = tabelValues;
    }

    public ObjectContainingString permissions(TabelValues tabelValues) {
        this.setPermissions(tabelValues);
        return this;
    }

    public Label getPermissions1() {
        return this.permissions1;
    }

    public void setPermissions1(Label label) {
        this.permissions1 = label;
    }

    public ObjectContainingString permissions1(Label label) {
        this.setPermissions1(label);
        return this;
    }

    public Spacing getJson1() {
        return this.json1;
    }

    public void setJson1(Spacing spacing) {
        this.json1 = spacing;
    }

    public ObjectContainingString json1(Spacing spacing) {
        this.setJson1(spacing);
        return this;
    }

    public Spacing getJson2() {
        return this.json2;
    }

    public void setJson2(Spacing spacing) {
        this.json2 = spacing;
    }

    public ObjectContainingString json2(Spacing spacing) {
        this.setJson2(spacing);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ObjectContainingString)) {
            return false;
        }
        return id != null && id.equals(((ObjectContainingString) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ObjectContainingString{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
