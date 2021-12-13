package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Label.
 */
@Entity
@Table(name = "label")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Label implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(
        value = { "spacing", "attributes", "text", "badge", "icon", "image", "badgeTypes", "displayAtt" },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private DisplayAtt displayAtt;

    @JsonIgnoreProperties(value = { "margins", "paddings" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Spacing spacing;

    @JsonIgnoreProperties(value = { "attributes" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Text text;

    @JsonIgnoreProperties(value = { "attributes" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Icon icon;

    @OneToMany(mappedBy = "permissions1")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "json", "permissions", "permissions1", "json1", "json2" }, allowSetters = true)
    private Set<ObjectContainingString> objectContainingStrings = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "attributes", "labels" }, allowSetters = true)
    private Head labels;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Label id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DisplayAtt getDisplayAtt() {
        return this.displayAtt;
    }

    public void setDisplayAtt(DisplayAtt displayAtt) {
        this.displayAtt = displayAtt;
    }

    public Label displayAtt(DisplayAtt displayAtt) {
        this.setDisplayAtt(displayAtt);
        return this;
    }

    public Spacing getSpacing() {
        return this.spacing;
    }

    public void setSpacing(Spacing spacing) {
        this.spacing = spacing;
    }

    public Label spacing(Spacing spacing) {
        this.setSpacing(spacing);
        return this;
    }

    public Text getText() {
        return this.text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public Label text(Text text) {
        this.setText(text);
        return this;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Label icon(Icon icon) {
        this.setIcon(icon);
        return this;
    }

    public Set<ObjectContainingString> getObjectContainingStrings() {
        return this.objectContainingStrings;
    }

    public void setObjectContainingStrings(Set<ObjectContainingString> objectContainingStrings) {
        if (this.objectContainingStrings != null) {
            this.objectContainingStrings.forEach(i -> i.setPermissions1(null));
        }
        if (objectContainingStrings != null) {
            objectContainingStrings.forEach(i -> i.setPermissions1(this));
        }
        this.objectContainingStrings = objectContainingStrings;
    }

    public Label objectContainingStrings(Set<ObjectContainingString> objectContainingStrings) {
        this.setObjectContainingStrings(objectContainingStrings);
        return this;
    }

    public Label addObjectContainingString(ObjectContainingString objectContainingString) {
        this.objectContainingStrings.add(objectContainingString);
        objectContainingString.setPermissions1(this);
        return this;
    }

    public Label removeObjectContainingString(ObjectContainingString objectContainingString) {
        this.objectContainingStrings.remove(objectContainingString);
        objectContainingString.setPermissions1(null);
        return this;
    }

    public Head getLabels() {
        return this.labels;
    }

    public void setLabels(Head head) {
        this.labels = head;
    }

    public Label labels(Head head) {
        this.setLabels(head);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Label)) {
            return false;
        }
        return id != null && id.equals(((Label) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Label{" +
            "id=" + getId() +
            "}";
    }
}
