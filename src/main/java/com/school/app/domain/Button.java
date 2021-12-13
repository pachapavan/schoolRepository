package com.school.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Button.
 */
@Entity
@Table(name = "button")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Button implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_link")
    private Boolean isLink;

    @Column(name = "link")
    private String link;

    @Column(name = "display_text")
    private String displayText;

    @Column(name = "font_size")
    private String fontSize;

    @OneToOne
    @JoinColumn(unique = true)
    private Attributes attributes;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Button id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsLink() {
        return this.isLink;
    }

    public Button isLink(Boolean isLink) {
        this.setIsLink(isLink);
        return this;
    }

    public void setIsLink(Boolean isLink) {
        this.isLink = isLink;
    }

    public String getLink() {
        return this.link;
    }

    public Button link(String link) {
        this.setLink(link);
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDisplayText() {
        return this.displayText;
    }

    public Button displayText(String displayText) {
        this.setDisplayText(displayText);
        return this;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getFontSize() {
        return this.fontSize;
    }

    public Button fontSize(String fontSize) {
        this.setFontSize(fontSize);
        return this;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Button attributes(Attributes attributes) {
        this.setAttributes(attributes);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Button)) {
            return false;
        }
        return id != null && id.equals(((Button) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Button{" +
            "id=" + getId() +
            ", isLink='" + getIsLink() + "'" +
            ", link='" + getLink() + "'" +
            ", displayText='" + getDisplayText() + "'" +
            ", fontSize='" + getFontSize() + "'" +
            "}";
    }
}
