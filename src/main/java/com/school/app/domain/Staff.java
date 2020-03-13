package com.school.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Staff.
 */
@Entity
@Table(name = "staff")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "staff_id")
    private Long staffId;

    @Column(name = "staff_name")
    private String staffName;

    @Column(name = "phone_number")
    private Long phoneNumber;

    @Column(name = "address")
    private String address;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Column(name = "is_teaching_staff")
    private Boolean isTeachingStaff;

    @Column(name = "status")
    private String status;

    @Column(name = "salary")
    private Long salary;

    @OneToMany(mappedBy = "staff")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<StaffSalary> salaries = new HashSet<>();

    @OneToMany(mappedBy = "staff")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ClassName> teachers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStaffId() {
        return staffId;
    }

    public Staff staffId(Long staffId) {
        this.staffId = staffId;
        return this;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public Staff staffName(String staffName) {
        this.staffName = staffName;
        return this;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public Staff phoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public Staff address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public Staff photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public Staff photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Boolean isIsTeachingStaff() {
        return isTeachingStaff;
    }

    public Staff isTeachingStaff(Boolean isTeachingStaff) {
        this.isTeachingStaff = isTeachingStaff;
        return this;
    }

    public void setIsTeachingStaff(Boolean isTeachingStaff) {
        this.isTeachingStaff = isTeachingStaff;
    }

    public String getStatus() {
        return status;
    }

    public Staff status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getSalary() {
        return salary;
    }

    public Staff salary(Long salary) {
        this.salary = salary;
        return this;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Set<StaffSalary> getSalaries() {
        return salaries;
    }

    public Staff salaries(Set<StaffSalary> staffSalaries) {
        this.salaries = staffSalaries;
        return this;
    }

    public Staff addSalary(StaffSalary staffSalary) {
        this.salaries.add(staffSalary);
        staffSalary.setStaff(this);
        return this;
    }

    public Staff removeSalary(StaffSalary staffSalary) {
        this.salaries.remove(staffSalary);
        staffSalary.setStaff(null);
        return this;
    }

    public void setSalaries(Set<StaffSalary> staffSalaries) {
        this.salaries = staffSalaries;
    }

    public Set<ClassName> getTeachers() {
        return teachers;
    }

    public Staff teachers(Set<ClassName> classNames) {
        this.teachers = classNames;
        return this;
    }

    public Staff addTeacher(ClassName className) {
        this.teachers.add(className);
        className.setStaff(this);
        return this;
    }

    public Staff removeTeacher(ClassName className) {
        this.teachers.remove(className);
        className.setStaff(null);
        return this;
    }

    public void setTeachers(Set<ClassName> classNames) {
        this.teachers = classNames;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Staff)) {
            return false;
        }
        return id != null && id.equals(((Staff) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Staff{" +
            "id=" + getId() +
            ", staffId=" + getStaffId() +
            ", staffName='" + getStaffName() + "'" +
            ", phoneNumber=" + getPhoneNumber() +
            ", address='" + getAddress() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", isTeachingStaff='" + isIsTeachingStaff() + "'" +
            ", status='" + getStatus() + "'" +
            ", salary=" + getSalary() +
            "}";
    }
}
