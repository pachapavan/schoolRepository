package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Subject.
 */
@Entity
@Table(name = "subject")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "classname")
    private String classname;

    @Column(name = "section")
    private String section;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "subject_code")
    private String subjectCode;

    @Column(name = "subject_teacher")
    private String subjectTeacher;

    @ManyToOne
    @JsonIgnoreProperties(value = { "subjects", "classes", "student" }, allowSetters = true)
    private StudentMarkes studentMarkes;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Subject id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassname() {
        return this.classname;
    }

    public Subject classname(String classname) {
        this.setClassname(classname);
        return this;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSection() {
        return this.section;
    }

    public Subject section(String section) {
        this.setSection(section);
        return this;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubjectName() {
        return this.subjectName;
    }

    public Subject subjectName(String subjectName) {
        this.setSubjectName(subjectName);
        return this;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return this.subjectCode;
    }

    public Subject subjectCode(String subjectCode) {
        this.setSubjectCode(subjectCode);
        return this;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectTeacher() {
        return this.subjectTeacher;
    }

    public Subject subjectTeacher(String subjectTeacher) {
        this.setSubjectTeacher(subjectTeacher);
        return this;
    }

    public void setSubjectTeacher(String subjectTeacher) {
        this.subjectTeacher = subjectTeacher;
    }

    public StudentMarkes getStudentMarkes() {
        return this.studentMarkes;
    }

    public void setStudentMarkes(StudentMarkes studentMarkes) {
        this.studentMarkes = studentMarkes;
    }

    public Subject studentMarkes(StudentMarkes studentMarkes) {
        this.setStudentMarkes(studentMarkes);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subject)) {
            return false;
        }
        return id != null && id.equals(((Subject) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Subject{" +
            "id=" + getId() +
            ", classname='" + getClassname() + "'" +
            ", section='" + getSection() + "'" +
            ", subjectName='" + getSubjectName() + "'" +
            ", subjectCode='" + getSubjectCode() + "'" +
            ", subjectTeacher='" + getSubjectTeacher() + "'" +
            "}";
    }
}
