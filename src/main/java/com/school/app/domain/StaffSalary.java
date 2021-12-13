package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StaffSalary.
 */
@Entity
@Table(name = "staff_salary")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StaffSalary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "salary_paid")
    private Long salaryPaid;

    @Column(name = "month")
    private String month;

    @ManyToOne
    @JsonIgnoreProperties(value = { "salaries", "teachers" }, allowSetters = true)
    private Staff staff;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StaffSalary id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSalaryPaid() {
        return this.salaryPaid;
    }

    public StaffSalary salaryPaid(Long salaryPaid) {
        this.setSalaryPaid(salaryPaid);
        return this;
    }

    public void setSalaryPaid(Long salaryPaid) {
        this.salaryPaid = salaryPaid;
    }

    public String getMonth() {
        return this.month;
    }

    public StaffSalary month(String month) {
        this.setMonth(month);
        return this;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Staff getStaff() {
        return this.staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public StaffSalary staff(Staff staff) {
        this.setStaff(staff);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StaffSalary{" +
            "id=" + getId() +
            ", salaryPaid=" + getSalaryPaid() +
            ", month='" + getMonth() + "'" +
            "}";
    }
}
