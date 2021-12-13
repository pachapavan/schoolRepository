package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ClassName.
 */
@Entity
@Table(name = "class_name")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClassName implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "class_number")
    private Long classNumber;

    @OneToMany(mappedBy = "className")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "className" }, allowSetters = true)
    private Set<Section> sections = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "classes", "markes", "attendences", "fees", "busRouteNames" }, allowSetters = true)
    private Student student;

    @ManyToOne
    @JsonIgnoreProperties(value = { "subjects", "classes", "student" }, allowSetters = true)
    private StudentMarkes studentMarkes;

    @ManyToOne
    @JsonIgnoreProperties(value = { "salaries", "teachers" }, allowSetters = true)
    private Staff staff;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClassName id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ClassName name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getClassNumber() {
        return this.classNumber;
    }

    public ClassName classNumber(Long classNumber) {
        this.setClassNumber(classNumber);
        return this;
    }

    public void setClassNumber(Long classNumber) {
        this.classNumber = classNumber;
    }

    public Set<Section> getSections() {
        return this.sections;
    }

    public void setSections(Set<Section> sections) {
        if (this.sections != null) {
            this.sections.forEach(i -> i.setClassName(null));
        }
        if (sections != null) {
            sections.forEach(i -> i.setClassName(this));
        }
        this.sections = sections;
    }

    public ClassName sections(Set<Section> sections) {
        this.setSections(sections);
        return this;
    }

    public ClassName addSection(Section section) {
        this.sections.add(section);
        section.setClassName(this);
        return this;
    }

    public ClassName removeSection(Section section) {
        this.sections.remove(section);
        section.setClassName(null);
        return this;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ClassName student(Student student) {
        this.setStudent(student);
        return this;
    }

    public StudentMarkes getStudentMarkes() {
        return this.studentMarkes;
    }

    public void setStudentMarkes(StudentMarkes studentMarkes) {
        this.studentMarkes = studentMarkes;
    }

    public ClassName studentMarkes(StudentMarkes studentMarkes) {
        this.setStudentMarkes(studentMarkes);
        return this;
    }

    public Staff getStaff() {
        return this.staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public ClassName staff(Staff staff) {
        this.setStaff(staff);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassName)) {
            return false;
        }
        return id != null && id.equals(((ClassName) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassName{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", classNumber=" + getClassNumber() +
            "}";
    }
}
