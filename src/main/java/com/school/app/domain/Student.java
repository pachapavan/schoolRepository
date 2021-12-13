package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "parent_name")
    private String parentName;

    @Column(name = "phone_number")
    private Long phoneNumber;

    @Column(name = "address")
    private String address;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Column(name = "status")
    private String status;

    @Column(name = "comments")
    private String comments;

    @OneToMany(mappedBy = "student")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sections", "student", "studentMarkes", "staff" }, allowSetters = true)
    private Set<ClassName> classes = new HashSet<>();

    @OneToMany(mappedBy = "student")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subjects", "classes", "student" }, allowSetters = true)
    private Set<StudentMarkes> markes = new HashSet<>();

    @OneToMany(mappedBy = "student")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "student" }, allowSetters = true)
    private Set<Attendence> attendences = new HashSet<>();

    @OneToMany(mappedBy = "student")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "student" }, allowSetters = true)
    private Set<StudentFee> fees = new HashSet<>();

    @OneToMany(mappedBy = "student")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "busRoutes", "busStops", "student" }, allowSetters = true)
    private Set<BusRoute> busRouteNames = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Student id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return this.studentId;
    }

    public Student studentId(Long studentId) {
        this.setStudentId(studentId);
        return this;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public Student studentName(String studentName) {
        this.setStudentName(studentName);
        return this;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getParentName() {
        return this.parentName;
    }

    public Student parentName(String parentName) {
        this.setParentName(parentName);
        return this;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getPhoneNumber() {
        return this.phoneNumber;
    }

    public Student phoneNumber(Long phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public Student address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Student photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Student photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getStatus() {
        return this.status;
    }

    public Student status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return this.comments;
    }

    public Student comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Set<ClassName> getClasses() {
        return this.classes;
    }

    public void setClasses(Set<ClassName> classNames) {
        if (this.classes != null) {
            this.classes.forEach(i -> i.setStudent(null));
        }
        if (classNames != null) {
            classNames.forEach(i -> i.setStudent(this));
        }
        this.classes = classNames;
    }

    public Student classes(Set<ClassName> classNames) {
        this.setClasses(classNames);
        return this;
    }

    public Student addClass(ClassName className) {
        this.classes.add(className);
        className.setStudent(this);
        return this;
    }

    public Student removeClass(ClassName className) {
        this.classes.remove(className);
        className.setStudent(null);
        return this;
    }

    public Set<StudentMarkes> getMarkes() {
        return this.markes;
    }

    public void setMarkes(Set<StudentMarkes> studentMarkes) {
        if (this.markes != null) {
            this.markes.forEach(i -> i.setStudent(null));
        }
        if (studentMarkes != null) {
            studentMarkes.forEach(i -> i.setStudent(this));
        }
        this.markes = studentMarkes;
    }

    public Student markes(Set<StudentMarkes> studentMarkes) {
        this.setMarkes(studentMarkes);
        return this;
    }

    public Student addMarkes(StudentMarkes studentMarkes) {
        this.markes.add(studentMarkes);
        studentMarkes.setStudent(this);
        return this;
    }

    public Student removeMarkes(StudentMarkes studentMarkes) {
        this.markes.remove(studentMarkes);
        studentMarkes.setStudent(null);
        return this;
    }

    public Set<Attendence> getAttendences() {
        return this.attendences;
    }

    public void setAttendences(Set<Attendence> attendences) {
        if (this.attendences != null) {
            this.attendences.forEach(i -> i.setStudent(null));
        }
        if (attendences != null) {
            attendences.forEach(i -> i.setStudent(this));
        }
        this.attendences = attendences;
    }

    public Student attendences(Set<Attendence> attendences) {
        this.setAttendences(attendences);
        return this;
    }

    public Student addAttendence(Attendence attendence) {
        this.attendences.add(attendence);
        attendence.setStudent(this);
        return this;
    }

    public Student removeAttendence(Attendence attendence) {
        this.attendences.remove(attendence);
        attendence.setStudent(null);
        return this;
    }

    public Set<StudentFee> getFees() {
        return this.fees;
    }

    public void setFees(Set<StudentFee> studentFees) {
        if (this.fees != null) {
            this.fees.forEach(i -> i.setStudent(null));
        }
        if (studentFees != null) {
            studentFees.forEach(i -> i.setStudent(this));
        }
        this.fees = studentFees;
    }

    public Student fees(Set<StudentFee> studentFees) {
        this.setFees(studentFees);
        return this;
    }

    public Student addFee(StudentFee studentFee) {
        this.fees.add(studentFee);
        studentFee.setStudent(this);
        return this;
    }

    public Student removeFee(StudentFee studentFee) {
        this.fees.remove(studentFee);
        studentFee.setStudent(null);
        return this;
    }

    public Set<BusRoute> getBusRouteNames() {
        return this.busRouteNames;
    }

    public void setBusRouteNames(Set<BusRoute> busRoutes) {
        if (this.busRouteNames != null) {
            this.busRouteNames.forEach(i -> i.setStudent(null));
        }
        if (busRoutes != null) {
            busRoutes.forEach(i -> i.setStudent(this));
        }
        this.busRouteNames = busRoutes;
    }

    public Student busRouteNames(Set<BusRoute> busRoutes) {
        this.setBusRouteNames(busRoutes);
        return this;
    }

    public Student addBusRouteName(BusRoute busRoute) {
        this.busRouteNames.add(busRoute);
        busRoute.setStudent(this);
        return this;
    }

    public Student removeBusRouteName(BusRoute busRoute) {
        this.busRouteNames.remove(busRoute);
        busRoute.setStudent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return id != null && id.equals(((Student) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", studentId=" + getStudentId() +
            ", studentName='" + getStudentName() + "'" +
            ", parentName='" + getParentName() + "'" +
            ", phoneNumber=" + getPhoneNumber() +
            ", address='" + getAddress() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", status='" + getStatus() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
