package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BusStops.
 */
@Entity
@Table(name = "bus_stops")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BusStops implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "route_name")
    private String routeName;

    @Column(name = "bus_stops")
    private String busStops;

    @ManyToOne
    @JsonIgnoreProperties(value = { "busRoutes", "busStops", "student" }, allowSetters = true)
    private BusRoute busRoute;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BusStops id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouteName() {
        return this.routeName;
    }

    public BusStops routeName(String routeName) {
        this.setRouteName(routeName);
        return this;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getBusStops() {
        return this.busStops;
    }

    public BusStops busStops(String busStops) {
        this.setBusStops(busStops);
        return this;
    }

    public void setBusStops(String busStops) {
        this.busStops = busStops;
    }

    public BusRoute getBusRoute() {
        return this.busRoute;
    }

    public void setBusRoute(BusRoute busRoute) {
        this.busRoute = busRoute;
    }

    public BusStops busRoute(BusRoute busRoute) {
        this.setBusRoute(busRoute);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusStops)) {
            return false;
        }
        return id != null && id.equals(((BusStops) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusStops{" +
            "id=" + getId() +
            ", routeName='" + getRouteName() + "'" +
            ", busStops='" + getBusStops() + "'" +
            "}";
    }
}
