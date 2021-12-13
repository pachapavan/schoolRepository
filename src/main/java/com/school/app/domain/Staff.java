package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Staff.
 */
@Entity
@Table(name = "staff")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "staff" }, allowSetters = true)
    private Set<StaffSalary> salaries = new HashSet<>();

    @OneToMany(mappedBy = "staff")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sections", "student", "studentMarkes", "staff" }, allowSetters = true)
    private Set<ClassName> teachers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Staff id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStaffId() {
        return this.staffId;
    }

    public Staff staffId(Long staffId) {
        this.setStaffId(staffId);
        return this;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return this.staffName;
    }

    public Staff staffName(String staffName) {
        this.setStaffName(staffName);
        return this;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Long getPhoneNumber() {
        return this.phoneNumber;
    }

    public Staff phoneNumber(Long phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public Staff address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Staff photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Staff photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Boolean getIsTeachingStaff() {
        return this.isTeachingStaff;
    }

    public Staff isTeachingStaff(Boolean isTeachingStaff) {
        this.setIsTeachingStaff(isTeachingStaff);
        return this;
    }

    public void setIsTeachingStaff(Boolean isTeachingStaff) {
        this.isTeachingStaff = isTeachingStaff;
    }

    public String getStatus() {
        return this.status;
    }

    public Staff status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getSalary() {
        return this.salary;
    }

    public Staff salary(Long salary) {
        this.setSalary(salary);
        return this;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Set<StaffSalary> getSalaries() {
        return this.salaries;
    }

    public void setSalaries(Set<StaffSalary> staffSalaries) {
        if (this.salaries != null) {
            this.salaries.forEach(i -> i.setStaff(null));
        }
        if (staffSalaries != null) {
            staffSalaries.forEach(i -> i.setStaff(this));
        }
        this.salaries = staffSalaries;
    }

    public Staff salaries(Set<StaffSalary> staffSalaries) {
        this.setSalaries(staffSalaries);
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

    public Set<ClassName> getTeachers() {
        return this.teachers;
    }

    public void setTeachers(Set<ClassName> classNames) {
        if (this.teachers != null) {
            this.teachers.forEach(i -> i.setStaff(null));
        }
        if (classNames != null) {
            classNames.forEach(i -> i.setStaff(this));
        }
        this.teachers = classNames;
    }

    public Staff teachers(Set<ClassName> classNames) {
        this.setTeachers(classNames);
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
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
            ", isTeachingStaff='" + getIsTeachingStaff() + "'" +
            ", status='" + getStatus() + "'" +
            ", salary=" + getSalary() +
            "}";
    }
}
