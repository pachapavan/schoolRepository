package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.school.app.domain.enumeration.ColorEnum;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BadgeType.
 */
@Entity
@Table(name = "badge_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BadgeType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private String status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ColorEnum type;

    @OneToOne
    @JoinColumn(unique = true)
    private Attributes attributes;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "spacing", "attributes", "text", "badge", "icon", "image", "badgeTypes", "displayAtt" },
        allowSetters = true
    )
    private DisplayAtt badgeType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BadgeType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return this.status;
    }

    public BadgeType status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ColorEnum getType() {
        return this.type;
    }

    public BadgeType type(ColorEnum type) {
        this.setType(type);
        return this;
    }

    public void setType(ColorEnum type) {
        this.type = type;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public BadgeType attributes(Attributes attributes) {
        this.setAttributes(attributes);
        return this;
    }

    public DisplayAtt getBadgeType() {
        return this.badgeType;
    }

    public void setBadgeType(DisplayAtt displayAtt) {
        this.badgeType = displayAtt;
    }

    public BadgeType badgeType(DisplayAtt displayAtt) {
        this.setBadgeType(displayAtt);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BadgeType)) {
            return false;
        }
        return id != null && id.equals(((BadgeType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BadgeType{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
