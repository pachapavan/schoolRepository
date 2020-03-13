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
 * A StudentMarkes.
 */
@Entity
@Table(name = "student_markes")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StudentMarkes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_name")
    private String examName;

    @Column(name = "total_markes")
    private Long totalMarkes;

    @Column(name = "markes")
    private Long markes;

    @Column(name = "comments")
    private String comments;

    @OneToMany(mappedBy = "studentMarkes")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Subject> subjects = new HashSet<>();

    @OneToMany(mappedBy = "studentMarkes")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ClassName> classes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("markes")
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExamName() {
        return examName;
    }

    public StudentMarkes examName(String examName) {
        this.examName = examName;
        return this;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Long getTotalMarkes() {
        return totalMarkes;
    }

    public StudentMarkes totalMarkes(Long totalMarkes) {
        this.totalMarkes = totalMarkes;
        return this;
    }

    public void setTotalMarkes(Long totalMarkes) {
        this.totalMarkes = totalMarkes;
    }

    public Long getMarkes() {
        return markes;
    }

    public StudentMarkes markes(Long markes) {
        this.markes = markes;
        return this;
    }

    public void setMarkes(Long markes) {
        this.markes = markes;
    }

    public String getComments() {
        return comments;
    }

    public StudentMarkes comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public StudentMarkes subjects(Set<Subject> subjects) {
        this.subjects = subjects;
        return this;
    }

    public StudentMarkes addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.setStudentMarkes(this);
        return this;
    }

    public StudentMarkes removeSubject(Subject subject) {
        this.subjects.remove(subject);
        subject.setStudentMarkes(null);
        return this;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public Set<ClassName> getClasses() {
        return classes;
    }

    public StudentMarkes classes(Set<ClassName> classNames) {
        this.classes = classNames;
        return this;
    }

    public StudentMarkes addClass(ClassName className) {
        this.classes.add(className);
        className.setStudentMarkes(this);
        return this;
    }

    public StudentMarkes removeClass(ClassName className) {
        this.classes.remove(className);
        className.setStudentMarkes(null);
        return this;
    }

    public void setClasses(Set<ClassName> classNames) {
        this.classes = classNames;
    }

    public Student getStudent() {
        return student;
    }

    public StudentMarkes student(Student student) {
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
        if (!(o instanceof StudentMarkes)) {
            return false;
        }
        return id != null && id.equals(((StudentMarkes) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StudentMarkes{" +
            "id=" + getId() +
            ", examName='" + getExamName() + "'" +
            ", totalMarkes=" + getTotalMarkes() +
            ", markes=" + getMarkes() +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
