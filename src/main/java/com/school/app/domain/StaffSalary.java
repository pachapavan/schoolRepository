package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A StaffSalary.
 */
@Entity
@Table(name = "staff_salary")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StaffSalary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "salary_paid")
    private Long salaryPaid;

    @Column(name = "month")
    private String month;

    @ManyToOne
    @JsonIgnoreProperties("salaries")
    private Staff staff;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSalaryPaid() {
        return salaryPaid;
    }

    public StaffSalary salaryPaid(Long salaryPaid) {
        this.salaryPaid = salaryPaid;
        return this;
    }

    public void setSalaryPaid(Long salaryPaid) {
        this.salaryPaid = salaryPaid;
    }

    public String getMonth() {
        return month;
    }

    public StaffSalary month(String month) {
        this.month = month;
        return this;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Staff getStaff() {
        return staff;
    }

    public StaffSalary staff(Staff staff) {
        this.staff = staff;
        return this;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StaffSalary)) {
            return false;
        }
        return id != null && id.equals(((StaffSalary) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StaffSalary{" +
            "id=" + getId() +
            ", salaryPaid=" + getSalaryPaid() +
            ", month='" + getMonth() + "'" +
            "}";
    }
}
