package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A ClassName.
 */
@Entity
@Table(name = "class_name")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClassName implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "class_number")
    private Long classNumber;

    @OneToMany(mappedBy = "className")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Section> sections = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("classes")
    private Student student;

    @ManyToOne
    @JsonIgnoreProperties("classes")
    private StudentMarkes studentMarkes;

    @ManyToOne
    @JsonIgnoreProperties("teachers")
    private Staff staff;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ClassName name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getClassNumber() {
        return classNumber;
    }

    public ClassName classNumber(Long classNumber) {
        this.classNumber = classNumber;
        return this;
    }

    public void setClassNumber(Long classNumber) {
        this.classNumber = classNumber;
    }

    public Set<Section> getSections() {
        return sections;
    }

    public ClassName sections(Set<Section> sections) {
        this.sections = sections;
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

    public void setSections(Set<Section> sections) {
        this.sections = sections;
    }

    public Student getStudent() {
        return student;
    }

    public ClassName student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentMarkes getStudentMarkes() {
        return studentMarkes;
    }

    public ClassName studentMarkes(StudentMarkes studentMarkes) {
        this.studentMarkes = studentMarkes;
        return this;
    }

    public void setStudentMarkes(StudentMarkes studentMarkes) {
        this.studentMarkes = studentMarkes;
    }

    public Staff getStaff() {
        return staff;
    }

    public ClassName staff(Staff staff) {
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
        if (!(o instanceof ClassName)) {
            return false;
        }
        return id != null && id.equals(((ClassName) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ClassName{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", classNumber=" + getClassNumber() +
            "}";
    }
}
