package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BusRoute.
 */
@Entity
@Table(name = "bus_route")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BusRoute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "route_name")
    private String routeName;

    @Column(name = "route_driver")
    private String routeDriver;

    @Column(name = "bus_number")
    private Long busNumber;

    @Column(name = "year")
    private Long year;

    @Column(name = "status")
    private String status;

    @Column(name = "comments")
    private String comments;

    @OneToMany(mappedBy = "busRoute")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "busRoute" }, allowSetters = true)
    private Set<BusRouteName> busRoutes = new HashSet<>();

    @OneToMany(mappedBy = "busRoute")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "busRoute" }, allowSetters = true)
    private Set<BusStops> busStops = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "classes", "markes", "attendences", "fees", "busRouteNames" }, allowSetters = true)
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BusRoute id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouteName() {
        return this.routeName;
    }

    public BusRoute routeName(String routeName) {
        this.setRouteName(routeName);
        return this;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteDriver() {
        return this.routeDriver;
    }

    public BusRoute routeDriver(String routeDriver) {
        this.setRouteDriver(routeDriver);
        return this;
    }

    public void setRouteDriver(String routeDriver) {
        this.routeDriver = routeDriver;
    }

    public Long getBusNumber() {
        return this.busNumber;
    }

    public BusRoute busNumber(Long busNumber) {
        this.setBusNumber(busNumber);
        return this;
    }

    public void setBusNumber(Long busNumber) {
        this.busNumber = busNumber;
    }

    public Long getYear() {
        return this.year;
    }

    public BusRoute year(Long year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getStatus() {
        return this.status;
    }

    public BusRoute status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return this.comments;
    }

    public BusRoute comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Set<BusRouteName> getBusRoutes() {
        return this.busRoutes;
    }

    public void setBusRoutes(Set<BusRouteName> busRouteNames) {
        if (this.busRoutes != null) {
            this.busRoutes.forEach(i -> i.setBusRoute(null));
        }
        if (busRouteNames != null) {
            busRouteNames.forEach(i -> i.setBusRoute(this));
        }
        this.busRoutes = busRouteNames;
    }

    public BusRoute busRoutes(Set<BusRouteName> busRouteNames) {
        this.setBusRoutes(busRouteNames);
        return this;
    }

    public BusRoute addBusRoute(BusRouteName busRouteName) {
        this.busRoutes.add(busRouteName);
        busRouteName.setBusRoute(this);
        return this;
    }

    public BusRoute removeBusRoute(BusRouteName busRouteName) {
        this.busRoutes.remove(busRouteName);
        busRouteName.setBusRoute(null);
        return this;
    }

    public Set<BusStops> getBusStops() {
        return this.busStops;
    }

    public void setBusStops(Set<BusStops> busStops) {
        if (this.busStops != null) {
            this.busStops.forEach(i -> i.setBusRoute(null));
        }
        if (busStops != null) {
            busStops.forEach(i -> i.setBusRoute(this));
        }
        this.busStops = busStops;
    }

    public BusRoute busStops(Set<BusStops> busStops) {
        this.setBusStops(busStops);
        return this;
    }

    public BusRoute addBusStop(BusStops busStops) {
        this.busStops.add(busStops);
        busStops.setBusRoute(this);
        return this;
    }

    public BusRoute removeBusStop(BusStops busStops) {
        this.busStops.remove(busStops);
        busStops.setBusRoute(null);
        return this;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public BusRoute student(Student student) {
        this.setStudent(student);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusRoute)) {
            return false;
        }
        return id != null && id.equals(((BusRoute) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusRoute{" +
            "id=" + getId() +
            ", routeName='" + getRouteName() + "'" +
            ", routeDriver='" + getRouteDriver() + "'" +
            ", busNumber=" + getBusNumber() +
            ", year=" + getYear() +
            ", status='" + getStatus() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
