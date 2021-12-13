package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StudentFee.
 */
@Entity
@Table(name = "student_fee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
    @JsonIgnoreProperties(value = { "classes", "markes", "attendences", "fees", "busRouteNames" }, allowSetters = true)
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudentFee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalAcademicFee() {
        return this.totalAcademicFee;
    }

    public StudentFee totalAcademicFee(Long totalAcademicFee) {
        this.setTotalAcademicFee(totalAcademicFee);
        return this;
    }

    public void setTotalAcademicFee(Long totalAcademicFee) {
        this.totalAcademicFee = totalAcademicFee;
    }

    public Long getAcademicFeewaveOff() {
        return this.academicFeewaveOff;
    }

    public StudentFee academicFeewaveOff(Long academicFeewaveOff) {
        this.setAcademicFeewaveOff(academicFeewaveOff);
        return this;
    }

    public void setAcademicFeewaveOff(Long academicFeewaveOff) {
        this.academicFeewaveOff = academicFeewaveOff;
    }

    public Long getAcademicFeePaid() {
        return this.academicFeePaid;
    }

    public StudentFee academicFeePaid(Long academicFeePaid) {
        this.setAcademicFeePaid(academicFeePaid);
        return this;
    }

    public void setAcademicFeePaid(Long academicFeePaid) {
        this.academicFeePaid = academicFeePaid;
    }

    public Long getTotalAcademicFeePaid() {
        return this.totalAcademicFeePaid;
    }

    public StudentFee totalAcademicFeePaid(Long totalAcademicFeePaid) {
        this.setTotalAcademicFeePaid(totalAcademicFeePaid);
        return this;
    }

    public void setTotalAcademicFeePaid(Long totalAcademicFeePaid) {
        this.totalAcademicFeePaid = totalAcademicFeePaid;
    }

    public Long getAcademicFeepending() {
        return this.academicFeepending;
    }

    public StudentFee academicFeepending(Long academicFeepending) {
        this.setAcademicFeepending(academicFeepending);
        return this;
    }

    public void setAcademicFeepending(Long academicFeepending) {
        this.academicFeepending = academicFeepending;
    }

    public Boolean getBusAlloted() {
        return this.busAlloted;
    }

    public StudentFee busAlloted(Boolean busAlloted) {
        this.setBusAlloted(busAlloted);
        return this;
    }

    public void setBusAlloted(Boolean busAlloted) {
        this.busAlloted = busAlloted;
    }

    public Boolean getHostelAlloted() {
        return this.hostelAlloted;
    }

    public StudentFee hostelAlloted(Boolean hostelAlloted) {
        this.setHostelAlloted(hostelAlloted);
        return this;
    }

    public void setHostelAlloted(Boolean hostelAlloted) {
        this.hostelAlloted = hostelAlloted;
    }

    public Long getTotalBusFee() {
        return this.totalBusFee;
    }

    public StudentFee totalBusFee(Long totalBusFee) {
        this.setTotalBusFee(totalBusFee);
        return this;
    }

    public void setTotalBusFee(Long totalBusFee) {
        this.totalBusFee = totalBusFee;
    }

    public Long getBusFeewaveOff() {
        return this.busFeewaveOff;
    }

    public StudentFee busFeewaveOff(Long busFeewaveOff) {
        this.setBusFeewaveOff(busFeewaveOff);
        return this;
    }

    public void setBusFeewaveOff(Long busFeewaveOff) {
        this.busFeewaveOff = busFeewaveOff;
    }

    public Long getBusFeePaid() {
        return this.busFeePaid;
    }

    public StudentFee busFeePaid(Long busFeePaid) {
        this.setBusFeePaid(busFeePaid);
        return this;
    }

    public void setBusFeePaid(Long busFeePaid) {
        this.busFeePaid = busFeePaid;
    }

    public Long getTotalBusFeePaid() {
        return this.totalBusFeePaid;
    }

    public StudentFee totalBusFeePaid(Long totalBusFeePaid) {
        this.setTotalBusFeePaid(totalBusFeePaid);
        return this;
    }

    public void setTotalBusFeePaid(Long totalBusFeePaid) {
        this.totalBusFeePaid = totalBusFeePaid;
    }

    public Long getBusFeepending() {
        return this.busFeepending;
    }

    public StudentFee busFeepending(Long busFeepending) {
        this.setBusFeepending(busFeepending);
        return this;
    }

    public void setBusFeepending(Long busFeepending) {
        this.busFeepending = busFeepending;
    }

    public Long getTotalHostelFee() {
        return this.totalHostelFee;
    }

    public StudentFee totalHostelFee(Long totalHostelFee) {
        this.setTotalHostelFee(totalHostelFee);
        return this;
    }

    public void setTotalHostelFee(Long totalHostelFee) {
        this.totalHostelFee = totalHostelFee;
    }

    public Long getHostelFeewaveOff() {
        return this.hostelFeewaveOff;
    }

    public StudentFee hostelFeewaveOff(Long hostelFeewaveOff) {
        this.setHostelFeewaveOff(hostelFeewaveOff);
        return this;
    }

    public void setHostelFeewaveOff(Long hostelFeewaveOff) {
        this.hostelFeewaveOff = hostelFeewaveOff;
    }

    public Long getTotalHostelFeePaid() {
        return this.totalHostelFeePaid;
    }

    public StudentFee totalHostelFeePaid(Long totalHostelFeePaid) {
        this.setTotalHostelFeePaid(totalHostelFeePaid);
        return this;
    }

    public void setTotalHostelFeePaid(Long totalHostelFeePaid) {
        this.totalHostelFeePaid = totalHostelFeePaid;
    }

    public Long getHostelFeePaid() {
        return this.hostelFeePaid;
    }

    public StudentFee hostelFeePaid(Long hostelFeePaid) {
        this.setHostelFeePaid(hostelFeePaid);
        return this;
    }

    public void setHostelFeePaid(Long hostelFeePaid) {
        this.hostelFeePaid = hostelFeePaid;
    }

    public Long getHostelFeepending() {
        return this.hostelFeepending;
    }

    public StudentFee hostelFeepending(Long hostelFeepending) {
        this.setHostelFeepending(hostelFeepending);
        return this;
    }

    public void setHostelFeepending(Long hostelFeepending) {
        this.hostelFeepending = hostelFeepending;
    }

    public Long getHostelExpenses() {
        return this.hostelExpenses;
    }

    public StudentFee hostelExpenses(Long hostelExpenses) {
        this.setHostelExpenses(hostelExpenses);
        return this;
    }

    public void setHostelExpenses(Long hostelExpenses) {
        this.hostelExpenses = hostelExpenses;
    }

    public Long getYear() {
        return this.year;
    }

    public StudentFee year(Long year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getComments() {
        return this.comments;
    }

    public StudentFee comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentFee student(Student student) {
        this.setStudent(student);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentFee{" +
            "id=" + getId() +
            ", totalAcademicFee=" + getTotalAcademicFee() +
            ", academicFeewaveOff=" + getAcademicFeewaveOff() +
            ", academicFeePaid=" + getAcademicFeePaid() +
            ", totalAcademicFeePaid=" + getTotalAcademicFeePaid() +
            ", academicFeepending=" + getAcademicFeepending() +
            ", busAlloted='" + getBusAlloted() + "'" +
            ", hostelAlloted='" + getHostelAlloted() + "'" +
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
