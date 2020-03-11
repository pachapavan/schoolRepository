package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A BusStops.
 */
@Entity
@Table(name = "bus_stops")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BusStops implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_name")
    private String routeName;

    @Column(name = "bus_stops")
    private String busStops;

    @ManyToOne
    @JsonIgnoreProperties("busStops")
    private BusRoute busRoute;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouteName() {
        return routeName;
    }

    public BusStops routeName(String routeName) {
        this.routeName = routeName;
        return this;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getBusStops() {
        return busStops;
    }

    public BusStops busStops(String busStops) {
        this.busStops = busStops;
        return this;
    }

    public void setBusStops(String busStops) {
        this.busStops = busStops;
    }

    public BusRoute getBusRoute() {
        return busRoute;
    }

    public BusStops busRoute(BusRoute busRoute) {
        this.busRoute = busRoute;
        return this;
    }

    public void setBusRoute(BusRoute busRoute) {
        this.busRoute = busRoute;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
        return 31;
    }

    @Override
    public String toString() {
        return "BusStops{" +
            "id=" + getId() +
            ", routeName='" + getRouteName() + "'" +
            ", busStops='" + getBusStops() + "'" +
            "}";
    }
}
