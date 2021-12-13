package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Attendence.
 */
@Entity
@Table(name = "attendence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Attendence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "month")
    private LocalDate month;

    @Column(name = "total_working_days")
    private Long totalWorkingDays;

    @Column(name = "dayspresent")
    private Long dayspresent;

    @ManyToOne
    @JsonIgnoreProperties(value = { "classes", "markes", "attendences", "fees", "busRouteNames" }, allowSetters = true)
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Attendence id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getMonth() {
        return this.month;
    }

    public Attendence month(LocalDate month) {
        this.setMonth(month);
        return this;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public Long getTotalWorkingDays() {
        return this.totalWorkingDays;
    }

    public Attendence totalWorkingDays(Long totalWorkingDays) {
        this.setTotalWorkingDays(totalWorkingDays);
        return this;
    }

    public void setTotalWorkingDays(Long totalWorkingDays) {
        this.totalWorkingDays = totalWorkingDays;
    }

    public Long getDayspresent() {
        return this.dayspresent;
    }

    public Attendence dayspresent(Long dayspresent) {
        this.setDayspresent(dayspresent);
        return this;
    }

    public void setDayspresent(Long dayspresent) {
        this.dayspresent = dayspresent;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Attendence student(Student student) {
        this.setStudent(student);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
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
