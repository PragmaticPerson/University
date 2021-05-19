package ua.com.foxminded.service.models.people;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Proxy;

import ua.com.foxminded.service.models.subject.Subject;
import ua.com.foxminded.validator.annotations.TeacherConstraint;

@Entity
@Table(name = "teachers")
@Proxy(lazy = false)
@TeacherConstraint
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private int id;

    @NotBlank(message = "Name is not valid")
    @Column(name = "first_name")
    private String name;

    @NotBlank(message = "Name is not valid")
    @Column(name = "last_name")
    private String surname;

    @NotNull(message = "Subjects cannot be null")
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "teachers_subjects", joinColumns = { @JoinColumn(name = "teacher_id") }, inverseJoinColumns = {
            @JoinColumn(name = "subject_id") })
    @OrderBy("subject_id ASC")
    private List<Subject> subjects;

    @Column(name = "deleted")
    private boolean deleted = false;

    public Teacher() {
    }

    public Teacher(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Teacher(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public void deleteSubject(Subject subject) {
        subjects.remove(subject);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (deleted ? 1231 : 1237);
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((subjects == null) ? 0 : subjects.hashCode());
        result = prime * result + ((surname == null) ? 0 : surname.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Teacher other = (Teacher) obj;
        if (deleted != other.deleted)
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (subjects == null) {
            if (other.subjects != null)
                return false;
        } else if (!subjects.equals(other.subjects))
            return false;
        if (surname == null) {
            if (other.surname != null)
                return false;
        } else if (!surname.equals(other.surname))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Teacher [id=" + id + ", name=" + name + ", surname=" + surname + ", subjects=" + subjects + "]";
    }
}
