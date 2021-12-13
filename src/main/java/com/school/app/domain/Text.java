package com.school.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Text.
 */
@Entity
@Table(name = "text")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Text implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_function")
    private Boolean isFunction;

    @Column(name = "display_text")
    private String displayText;

    @Column(name = "font_size")
    private String fontSize;

    @Column(name = "generic_object")
    private String genericObject;

    @Column(name = "function")
    private String function;

    @OneToOne
    @JoinColumn(unique = true)
    private Attributes attributes;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Text id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsFunction() {
        return this.isFunction;
    }

    public Text isFunction(Boolean isFunction) {
        this.setIsFunction(isFunction);
        return this;
    }

    public void setIsFunction(Boolean isFunction) {
        this.isFunction = isFunction;
    }

    public String getDisplayText() {
        return this.displayText;
    }

    public Text displayText(String displayText) {
        this.setDisplayText(displayText);
        return this;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getFontSize() {
        return this.fontSize;
    }

    public Text fontSize(String fontSize) {
        this.setFontSize(fontSize);
        return this;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getGenericObject() {
        return this.genericObject;
    }

    public Text genericObject(String genericObject) {
        this.setGenericObject(genericObject);
        return this;
    }

    public void setGenericObject(String genericObject) {
        this.genericObject = genericObject;
    }

    public String getFunction() {
        return this.function;
    }

    public Text function(String function) {
        this.setFunction(function);
        return this;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Text attributes(Attributes attributes) {
        this.setAttributes(attributes);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Text)) {
            return false;
        }
        return id != null && id.equals(((Text) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Text{" +
            "id=" + getId() +
            ", isFunction='" + getIsFunction() + "'" +
            ", displayText='" + getDisplayText() + "'" +
            ", fontSize='" + getFontSize() + "'" +
            ", genericObject='" + getGenericObject() + "'" +
            ", function='" + getFunction() + "'" +
            "}";
    }
}
