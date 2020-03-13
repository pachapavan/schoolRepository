package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A BusRoute.
 */
@Entity
@Table(name = "bus_route")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BusRoute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BusRouteName> busRoutes = new HashSet<>();

    @OneToMany(mappedBy = "busRoute")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BusStops> busStops = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("busRouteNames")
    private Student student;

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

    public BusRoute routeName(String routeName) {
        this.routeName = routeName;
        return this;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteDriver() {
        return routeDriver;
    }

    public BusRoute routeDriver(String routeDriver) {
        this.routeDriver = routeDriver;
        return this;
    }

    public void setRouteDriver(String routeDriver) {
        this.routeDriver = routeDriver;
    }

    public Long getBusNumber() {
        return busNumber;
    }

    public BusRoute busNumber(Long busNumber) {
        this.busNumber = busNumber;
        return this;
    }

    public void setBusNumber(Long busNumber) {
        this.busNumber = busNumber;
    }

    public Long getYear() {
        return year;
    }

    public BusRoute year(Long year) {
        this.year = year;
        return this;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public BusRoute status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public BusRoute comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Set<BusRouteName> getBusRoutes() {
        return busRoutes;
    }

    public BusRoute busRoutes(Set<BusRouteName> busRouteNames) {
        this.busRoutes = busRouteNames;
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

    public void setBusRoutes(Set<BusRouteName> busRouteNames) {
        this.busRoutes = busRouteNames;
    }

    public Set<BusStops> getBusStops() {
        return busStops;
    }

    public BusRoute busStops(Set<BusStops> busStops) {
        this.busStops = busStops;
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

    public void setBusStops(Set<BusStops> busStops) {
        this.busStops = busStops;
    }

    public Student getStudent() {
        return student;
    }

    public BusRoute student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
        return 31;
    }

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
