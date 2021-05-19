package ua.com.foxminded.service.models.faculty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Proxy;

import ua.com.foxminded.validator.annotations.FacultyConstraint;

@Entity
@Table(name = "faculty")
@Proxy(lazy = false)
@FacultyConstraint
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faculty_id")
    private int id;

    @NotBlank(message ="Faculty name not valid")
    @Column(name = "faculty_name")
    private String name;

    @NotBlank(message = "Dean firstname not valid")
    @Column(name = "dean_firstname")
    private String deanFirstName;

    @NotBlank(message = "Dean lastname not valid")
    @Column(name = "dean_lastname")
    private String deanLastName;

    @Column(name = "deleted")
    private boolean deleted = false;

    public Faculty() {
    }

    public Faculty(String name, String deanFirstName, String deanLastName) {
        this.name = name;
        this.deanFirstName = deanFirstName;
        this.deanLastName = deanLastName;
    }

    public Faculty(int id, String name, String deanFirstName, String deanLastName) {
        this.id = id;
        this.name = name;
        this.deanFirstName = deanFirstName;
        this.deanLastName = deanLastName;
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

    public String getDeanFirstName() {
        return deanFirstName;
    }

    public void setDeanFirstName(String deanFirstName) {
        this.deanFirstName = deanFirstName;
    }

    public String getDeanLastName() {
        return deanLastName;
    }

    public void setDeanLastName(String deanLastName) {
        this.deanLastName = deanLastName;
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
        result = prime * result + ((deanFirstName == null) ? 0 : deanFirstName.hashCode());
        result = prime * result + ((deanLastName == null) ? 0 : deanLastName.hashCode());
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
        Faculty other = (Faculty) obj;
        if (deanFirstName == null) {
            if (other.deanFirstName != null)
                return false;
        } else if (!deanFirstName.equals(other.deanFirstName))
            return false;
        if (deanLastName == null) {
            if (other.deanLastName != null)
                return false;
        } else if (!deanLastName.equals(other.deanLastName))
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
        return "Faculty [id=" + id + ", name=" + name + ", deanFirstName=" + deanFirstName + ", deanLastName="
                + deanLastName + ", deleted=" + deleted + "]";
    }
}