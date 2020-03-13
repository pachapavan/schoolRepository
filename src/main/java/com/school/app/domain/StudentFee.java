package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A StudentFee.
 */
@Entity
@Table(name = "student_fee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StudentFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_academic_fee")
    private Long totalAcademicFee;

    @Column(name = "academic_feewave_off")
    private Long academicFeewaveOff;

    @Column(name = "academic_fee_paid")
    private Long academicFeePaid;

    @Column(name = "total_academic_fee_paid")
    private Long totalAcademicFeePaid;

    @Column(name = "academic_feepending")
    private Long academicFeepending;

    @Column(name = "bus_alloted")
    private Boolean busAlloted;

    @Column(name = "hostel_alloted")
    private Boolean hostelAlloted;

    @Column(name = "total_bus_fee")
    private Long totalBusFee;

    @Column(name = "bus_feewave_off")
    private Long busFeewaveOff;

    @Column(name = "bus_fee_paid")
    private Long busFeePaid;

    @Column(name = "total_bus_fee_paid")
    private Long totalBusFeePaid;

    @Column(name = "bus_feepending")
    private Long busFeepending;

    @Column(name = "total_hostel_fee")
    private Long totalHostelFee;

    @Column(name = "hostel_feewave_off")
    private Long hostelFeewaveOff;

    @Column(name = "total_hostel_fee_paid")
    private Long totalHostelFeePaid;

    @Column(name = "hostel_fee_paid")
    private Long hostelFeePaid;

    @Column(name = "hostel_feepending")
    private Long hostelFeepending;

    @Column(name = "hostel_expenses")
    private Long hostelExpenses;

    @Column(name = "year")
    private Long year;

    @Column(name = "comments")
    private String comments;

    @ManyToOne
    @JsonIgnoreProperties("fees")
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalAcademicFee() {
        return totalAcademicFee;
    }

    public StudentFee totalAcademicFee(Long totalAcademicFee) {
        this.totalAcademicFee = totalAcademicFee;
        return this;
    }

    public void setTotalAcademicFee(Long totalAcademicFee) {
        this.totalAcademicFee = totalAcademicFee;
    }

    public Long getAcademicFeewaveOff() {
        return academicFeewaveOff;
    }

    public StudentFee academicFeewaveOff(Long academicFeewaveOff) {
        this.academicFeewaveOff = academicFeewaveOff;
        return this;
    }

    public void setAcademicFeewaveOff(Long academicFeewaveOff) {
        this.academicFeewaveOff = academicFeewaveOff;
    }

    public Long getAcademicFeePaid() {
        return academicFeePaid;
    }

    public StudentFee academicFeePaid(Long academicFeePaid) {
        this.academicFeePaid = academicFeePaid;
        return this;
    }

    public void setAcademicFeePaid(Long academicFeePaid) {
        this.academicFeePaid = academicFeePaid;
    }

    public Long getTotalAcademicFeePaid() {
        return totalAcademicFeePaid;
    }

    public StudentFee totalAcademicFeePaid(Long totalAcademicFeePaid) {
        this.totalAcademicFeePaid = totalAcademicFeePaid;
        return this;
    }

    public void setTotalAcademicFeePaid(Long totalAcademicFeePaid) {
        this.totalAcademicFeePaid = totalAcademicFeePaid;
    }

    public Long getAcademicFeepending() {
        return academicFeepending;
    }

    public StudentFee academicFeepending(Long academicFeepending) {
        this.academicFeepending = academicFeepending;
        return this;
    }

    public void setAcademicFeepending(Long academicFeepending) {
        this.academicFeepending = academicFeepending;
    }

    public Boolean isBusAlloted() {
        return busAlloted;
    }

    public StudentFee busAlloted(Boolean busAlloted) {
        this.busAlloted = busAlloted;
        return this;
    }

    public void setBusAlloted(Boolean busAlloted) {
        this.busAlloted = busAlloted;
    }

    public Boolean isHostelAlloted() {
        return hostelAlloted;
    }

    public StudentFee hostelAlloted(Boolean hostelAlloted) {
        this.hostelAlloted = hostelAlloted;
        return this;
    }

    public void setHostelAlloted(Boolean hostelAlloted) {
        this.hostelAlloted = hostelAlloted;
    }

    public Long getTotalBusFee() {
        return totalBusFee;
    }

    public StudentFee totalBusFee(Long totalBusFee) {
        this.totalBusFee = totalBusFee;
        return this;
    }

    public void setTotalBusFee(Long totalBusFee) {
        this.totalBusFee = totalBusFee;
    }

    public Long getBusFeewaveOff() {
        return busFeewaveOff;
    }

    public StudentFee busFeewaveOff(Long busFeewaveOff) {
        this.busFeewaveOff = busFeewaveOff;
        return this;
    }

    public void setBusFeewaveOff(Long busFeewaveOff) {
        this.busFeewaveOff = busFeewaveOff;
    }

    public Long getBusFeePaid() {
        return busFeePaid;
    }

    public StudentFee busFeePaid(Long busFeePaid) {
        this.busFeePaid = busFeePaid;
        return this;
    }

    public void setBusFeePaid(Long busFeePaid) {
        this.busFeePaid = busFeePaid;
    }

    public Long getTotalBusFeePaid() {
        return totalBusFeePaid;
    }

    public StudentFee totalBusFeePaid(Long totalBusFeePaid) {
        this.totalBusFeePaid = totalBusFeePaid;
        return this;
    }

    public void setTotalBusFeePaid(Long totalBusFeePaid) {
        this.totalBusFeePaid = totalBusFeePaid;
    }

    public Long getBusFeepending() {
        return busFeepending;
    }

    public StudentFee busFeepending(Long busFeepending) {
        this.busFeepending = busFeepending;
        return this;
    }

    public void setBusFeepending(Long busFeepending) {
        this.busFeepending = busFeepending;
    }

    public Long getTotalHostelFee() {
        return totalHostelFee;
    }

    public StudentFee totalHostelFee(Long totalHostelFee) {
        this.totalHostelFee = totalHostelFee;
        return this;
    }

    public void setTotalHostelFee(Long totalHostelFee) {
        this.totalHostelFee = totalHostelFee;
    }

    public Long getHostelFeewaveOff() {
        return hostelFeewaveOff;
    }

    public StudentFee hostelFeewaveOff(Long hostelFeewaveOff) {
        this.hostelFeewaveOff = hostelFeewaveOff;
        return this;
    }

    public void setHostelFeewaveOff(Long hostelFeewaveOff) {
        this.hostelFeewaveOff = hostelFeewaveOff;
    }

    public Long getTotalHostelFeePaid() {
        return totalHostelFeePaid;
    }

    public StudentFee totalHostelFeePaid(Long totalHostelFeePaid) {
        this.totalHostelFeePaid = totalHostelFeePaid;
        return this;
    }

    public void setTotalHostelFeePaid(Long totalHostelFeePaid) {
        this.totalHostelFeePaid = totalHostelFeePaid;
    }

    public Long getHostelFeePaid() {
        return hostelFeePaid;
    }

    public StudentFee hostelFeePaid(Long hostelFeePaid) {
        this.hostelFeePaid = hostelFeePaid;
        return this;
    }

    public void setHostelFeePaid(Long hostelFeePaid) {
        this.hostelFeePaid = hostelFeePaid;
    }

    public Long getHostelFeepending() {
        return hostelFeepending;
    }

    public StudentFee hostelFeepending(Long hostelFeepending) {
        this.hostelFeepending = hostelFeepending;
        return this;
    }

    public void setHostelFeepending(Long hostelFeepending) {
        this.hostelFeepending = hostelFeepending;
    }

    public Long getHostelExpenses() {
        return hostelExpenses;
    }

    public StudentFee hostelExpenses(Long hostelExpenses) {
        this.hostelExpenses = hostelExpenses;
        return this;
    }

    public void setHostelExpenses(Long hostelExpenses) {
        this.hostelExpenses = hostelExpenses;
    }

    public Long getYear() {
        return year;
    }

    public StudentFee year(Long year) {
        this.year = year;
        return this;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getComments() {
        return comments;
    }

    public StudentFee comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Student getStudent() {
        return student;
    }

    public StudentFee student(Student student) {
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
        if (!(o instanceof StudentFee)) {
            return false;
        }
        return id != null && id.equals(((StudentFee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StudentFee{" +
            "id=" + getId() +
            ", totalAcademicFee=" + getTotalAcademicFee() +
            ", academicFeewaveOff=" + getAcademicFeewaveOff() +
            ", academicFeePaid=" + getAcademicFeePaid() +
            ", totalAcademicFeePaid=" + getTotalAcademicFeePaid() +
            ", academicFeepending=" + getAcademicFeepending() +
            ", busAlloted='" + isBusAlloted() + "'" +
            ", hostelAlloted='" + isHostelAlloted() + "'" +
            ", totalBusFee=" + getTotalBusFee() +
            ", busFeewaveOff=" + getBusFeewaveOff() +
            ", busFeePaid=" + getBusFeePaid() +
            ", totalBusFeePaid=" + getTotalBusFeePaid() +
            ", busFeepending=" + getBusFeepending() +
            ", totalHostelFee=" + getTotalHostelFee() +
            ", hostelFeewaveOff=" + getHostelFeewaveOff() +
            ", totalHostelFeePaid=" + getTotalHostelFeePaid() +
            ", hostelFeePaid=" + getHostelFeePaid() +
            ", hostelFeepending=" + getHostelFeepending() +
            ", hostelExpenses=" + getHostelExpenses() +
            ", year=" + getYear() +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
