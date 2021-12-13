package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.school.app.domain.enumeration.ElementType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DisplayAtt.
 */
@Entity
@Table(name = "display_att")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DisplayAtt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ElementType type;

    @JsonIgnoreProperties(value = { "margins", "paddings" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Spacing spacing;

    @OneToOne
    @JoinColumn(unique = true)
    private Attributes attributes;

    @JsonIgnoreProperties(value = { "attributes" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Text text;

    @OneToOne
    @JoinColumn(unique = true)
    private Badge badge;

    @JsonIgnoreProperties(value = { "attributes" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Icon icon;

    @JsonIgnoreProperties(value = { "attributes" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Image image;

    @OneToMany(mappedBy = "badgeType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attributes", "badgeType" }, allowSetters = true)
    private Set<BadgeType> badgeTypes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "attributes", "objectContainingStrings", "displayAtts", "tableValues" }, allowSetters = true)
    private TabelValues displayAtt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DisplayAtt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DisplayAtt name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ElementType getType() {
        return this.type;
    }

    public DisplayAtt type(ElementType type) {
        this.setType(type);
        return this;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public Spacing getSpacing() {
        return this.spacing;
    }

    public void setSpacing(Spacing spacing) {
        this.spacing = spacing;
    }

    public DisplayAtt spacing(Spacing spacing) {
        this.setSpacing(spacing);
        return this;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public DisplayAtt attributes(Attributes attributes) {
        this.setAttributes(attributes);
        return this;
    }

    public Text getText() {
        return this.text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public DisplayAtt text(Text text) {
        this.setText(text);
        return this;
    }

    public Badge getBadge() {
        return this.badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public DisplayAtt badge(Badge badge) {
        this.setBadge(badge);
        return this;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public DisplayAtt icon(Icon icon) {
        this.setIcon(icon);
        return this;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public DisplayAtt image(Image image) {
        this.setImage(image);
        return this;
    }

    public Set<BadgeType> getBadgeTypes() {
        return this.badgeTypes;
    }

    public void setBadgeTypes(Set<BadgeType> badgeTypes) {
        if (this.badgeTypes != null) {
            this.badgeTypes.forEach(i -> i.setBadgeType(null));
        }
        if (badgeTypes != null) {
            badgeTypes.forEach(i -> i.setBadgeType(this));
        }
        this.badgeTypes = badgeTypes;
    }

    public DisplayAtt badgeTypes(Set<BadgeType> badgeTypes) {
        this.setBadgeTypes(badgeTypes);
        return this;
    }

    public DisplayAtt addBadgeType(BadgeType badgeType) {
        this.badgeTypes.add(badgeType);
        badgeType.setBadgeType(this);
        return this;
    }

    public DisplayAtt removeBadgeType(BadgeType badgeType) {
        this.badgeTypes.remove(badgeType);
        badgeType.setBadgeType(null);
        return this;
    }

    public TabelValues getDisplayAtt() {
        return this.displayAtt;
    }

    public void setDisplayAtt(TabelValues tabelValues) {
        this.displayAtt = tabelValues;
    }

    public DisplayAtt displayAtt(TabelValues tabelValues) {
        this.setDisplayAtt(tabelValues);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DisplayAtt)) {
            return false;
        }
        return id != null && id.equals(((DisplayAtt) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DisplayAtt{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
