package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

/**
 * A Attendence.
 */
@Entity
@Table(name = "attendence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Attendence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "month")
    private LocalDate month;

    @Column(name = "total_working_days")
    private Long totalWorkingDays;

    @Column(name = "dayspresent")
    private Long dayspresent;

    @ManyToOne
    @JsonIgnoreProperties("attendences")
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getMonth() {
        return month;
    }

    public Attendence month(LocalDate month) {
        this.month = month;
        return this;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public Long getTotalWorkingDays() {
        return totalWorkingDays;
    }

    public Attendence totalWorkingDays(Long totalWorkingDays) {
        this.totalWorkingDays = totalWorkingDays;
        return this;
    }

    public void setTotalWorkingDays(Long totalWorkingDays) {
        this.totalWorkingDays = totalWorkingDays;
    }

    public Long getDayspresent() {
        return dayspresent;
    }

    public Attendence dayspresent(Long dayspresent) {
        this.dayspresent = dayspresent;
        return this;
    }

    public void setDayspresent(Long dayspresent) {
        this.dayspresent = dayspresent;
    }

    public Student getStudent() {
        return student;
    }

    public Attendence student(Student student) {
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
        if (!(o instanceof Attendence)) {
            return false;
        }
        return id != null && id.equals(((Attendence) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Attendence{" +
            "id=" + getId() +
            ", month='" + getMonth() + "'" +
            ", totalWorkingDays=" + getTotalWorkingDays() +
            ", dayspresent=" + getDayspresent() +
            "}";
    }
}
