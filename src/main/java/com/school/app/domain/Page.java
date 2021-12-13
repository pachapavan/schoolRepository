package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Page.
 */
@Entity
@Table(name = "page")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Page implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "model_id")
    private String modelId;

    @Column(name = "page_id")
    private Integer pageId;

    @Column(name = "type")
    private String type;

    @Column(name = "full_screen")
    private Boolean fullScreen;

    @Column(name = "history")
    private String history;

    @OneToMany(mappedBy = "json")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "json", "permissions", "permissions1", "json1", "json2" }, allowSetters = true)
    private Set<ObjectContainingString> genericObjectsLists = new HashSet<>();

    @OneToMany(mappedBy = "page")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "spacing", "attributes", "elements", "page" }, allowSetters = true)
    private Set<FlexBox> flexBoxes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Page id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Page name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Page description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModelId() {
        return this.modelId;
    }

    public Page modelId(String modelId) {
        this.setModelId(modelId);
        return this;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public Integer getPageId() {
        return this.pageId;
    }

    public Page pageId(Integer pageId) {
        this.setPageId(pageId);
        return this;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public String getType() {
        return this.type;
    }

    public Page type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getFullScreen() {
        return this.fullScreen;
    }

    public Page fullScreen(Boolean fullScreen) {
        this.setFullScreen(fullScreen);
        return this;
    }

    public void setFullScreen(Boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public String getHistory() {
        return this.history;
    }

    public Page history(String history) {
        this.setHistory(history);
        return this;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public Set<ObjectContainingString> getGenericObjectsLists() {
        return this.genericObjectsLists;
    }

    public void setGenericObjectsLists(Set<ObjectContainingString> objectContainingStrings) {
        if (this.genericObjectsLists != null) {
            this.genericObjectsLists.forEach(i -> i.setJson(null));
        }
        if (objectContainingStrings != null) {
            objectContainingStrings.forEach(i -> i.setJson(this));
        }
        this.genericObjectsLists = objectContainingStrings;
    }

    public Page genericObjectsLists(Set<ObjectContainingString> objectContainingStrings) {
        this.setGenericObjectsLists(objectContainingStrings);
        return this;
    }

    public Page addGenericObjectsList(ObjectContainingString objectContainingString) {
        this.genericObjectsLists.add(objectContainingString);
        objectContainingString.setJson(this);
        return this;
    }

    public Page removeGenericObjectsList(ObjectContainingString objectContainingString) {
        this.genericObjectsLists.remove(objectContainingString);
        objectContainingString.setJson(null);
        return this;
    }

    public Set<FlexBox> getFlexBoxes() {
        return this.flexBoxes;
    }

    public void setFlexBoxes(Set<FlexBox> flexBoxes) {
        if (this.flexBoxes != null) {
            this.flexBoxes.forEach(i -> i.setPage(null));
        }
        if (flexBoxes != null) {
            flexBoxes.forEach(i -> i.setPage(this));
        }
        this.flexBoxes = flexBoxes;
    }

    public Page flexBoxes(Set<FlexBox> flexBoxes) {
        this.setFlexBoxes(flexBoxes);
        return this;
    }

    public Page addFlexBoxes(FlexBox flexBox) {
        this.flexBoxes.add(flexBox);
        flexBox.setPage(this);
        return this;
    }

    public Page removeFlexBoxes(FlexBox flexBox) {
        this.flexBoxes.remove(flexBox);
        flexBox.setPage(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Page)) {
            return false;
        }
        return id != null && id.equals(((Page) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Page{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", modelId='" + getModelId() + "'" +
            ", pageId=" + getPageId() +
            ", type='" + getType() + "'" +
            ", fullScreen='" + getFullScreen() + "'" +
            ", history='" + getHistory() + "'" +
            "}";
    }
}
