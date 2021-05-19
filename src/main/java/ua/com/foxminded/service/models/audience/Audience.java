package ua.com.foxminded.service.models.audience;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.annotations.Proxy;

import ua.com.foxminded.validator.annotations.AudienceConstraint;

@Entity
@Table(name = "audiences")
@Proxy(lazy = false)
@AudienceConstraint
public class Audience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audience_id")
    private int id;

    @Min(value = 0,message = "Audience's number must be positive")
    @Column(name = "number")
    private int number;

    @Min(value = 20, message = "Audience's capacity must be more, than 20")
    @Column(name = "capacity")
    private int capacity;

    @Column(name = "deleted")
    private boolean deleted = false;

    public Audience() {
    }

    public Audience(int number, int capacity) {
        this.number = number;
        this.capacity = capacity;
    }

    public Audience(int id, int number, int capacity) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + capacity;
        result = prime * result + id;
        result = prime * result + number;
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
        Audience other = (Audience) obj;
        if (capacity != other.capacity)
            return false;
        if (id != other.id)
            return false;
        if (number != other.number)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Audience [id=" + id + ", number=" + number + ", capacity=" + capacity + ", deleted=" + deleted + "]";
    }
}
