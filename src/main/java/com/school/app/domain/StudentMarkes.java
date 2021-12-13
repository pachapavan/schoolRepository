package com.school.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StudentMarkes.
 */
@Entity
@Table(name = "student_markes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentMarkes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "studentMarkes" }, allowSetters = true)
    private Set<Subject> subjects = new HashSet<>();

    @OneToMany(mappedBy = "studentMarkes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sections", "student", "studentMarkes", "staff" }, allowSetters = true)
    private Set<ClassName> classes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "classes", "markes", "attendences", "fees", "busRouteNames" }, allowSetters = true)
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudentMarkes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExamName() {
        return this.examName;
    }

    public StudentMarkes examName(String examName) {
        this.setExamName(examName);
        return this;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Long getTotalMarkes() {
        return this.totalMarkes;
    }

    public StudentMarkes totalMarkes(Long totalMarkes) {
        this.setTotalMarkes(totalMarkes);
        return this;
    }

    public void setTotalMarkes(Long totalMarkes) {
        this.totalMarkes = totalMarkes;
    }

    public Long getMarkes() {
        return this.markes;
    }

    public StudentMarkes markes(Long markes) {
        this.setMarkes(markes);
        return this;
    }

    public void setMarkes(Long markes) {
        this.markes = markes;
    }

    public String getComments() {
        return this.comments;
    }

    public StudentMarkes comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Set<Subject> getSubjects() {
        return this.subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        if (this.subjects != null) {
            this.subjects.forEach(i -> i.setStudentMarkes(null));
        }
        if (subjects != null) {
            subjects.forEach(i -> i.setStudentMarkes(this));
        }
        this.subjects = subjects;
    }

    public StudentMarkes subjects(Set<Subject> subjects) {
        this.setSubjects(subjects);
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

    public Set<ClassName> getClasses() {
        return this.classes;
    }

    public void setClasses(Set<ClassName> classNames) {
        if (this.classes != null) {
            this.classes.forEach(i -> i.setStudentMarkes(null));
        }
        if (classNames != null) {
            classNames.forEach(i -> i.setStudentMarkes(this));
        }
        this.classes = classNames;
    }

    public StudentMarkes classes(Set<ClassName> classNames) {
        this.setClasses(classNames);
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

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentMarkes student(Student student) {
        this.setStudent(student);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
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
