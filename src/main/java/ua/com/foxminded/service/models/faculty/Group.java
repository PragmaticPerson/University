package ua.com.foxminded.service.models.faculty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Proxy;

import ua.com.foxminded.validator.annotations.GroupConstraint;

@Entity
@Table(name = "groups")
@Proxy(lazy = false)
@GroupConstraint
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private int id;

    @NotBlank(message = "Group name is not valid")
    @Column(name = "group_name")
    private String name;

    @NotNull(message = "Faculty cannot be null")
    @OneToOne
    @JoinColumn(name = "faculty_id", referencedColumnName = "faculty_id")
    private Faculty faculty;

    @Column(name = "deleted")
    private boolean deleted = false;

    public Group() {
    }

    public Group(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Group(String name, Faculty faculty) {
        this.name = name;
        this.faculty = faculty;
    }

    public Group(int id, String name, Faculty faculty) {
        this.id = id;
        this.name = name;
        this.faculty = faculty;
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

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
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
        result = prime * result + ((faculty == null) ? 0 : faculty.hashCode());
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Group other = (Group) obj;
        if (faculty == null) {
            if (other.faculty != null)
                return false;
        } else if (!faculty.equals(other.faculty))
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Group [id=" + id + ", name=" + name + ", faculty=" + faculty + "]";
    }

}
