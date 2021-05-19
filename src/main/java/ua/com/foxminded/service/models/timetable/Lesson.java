package ua.com.foxminded.service.models.timetable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.annotations.Proxy;

import ua.com.foxminded.service.models.audience.Audience;
import ua.com.foxminded.service.models.faculty.Group;
import ua.com.foxminded.service.models.people.Teacher;
import ua.com.foxminded.service.models.subject.Subject;
import ua.com.foxminded.validator.annotations.LessonConstraint;

@Entity
@Table(name = "lessons")
@Proxy(lazy = false)
@LessonConstraint
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private Group group;

    @Column(name = "lesson_number")
    @Enumerated(EnumType.STRING)
    private LessonNumber lessonNumber;

    @Column(name = "day")
    @Enumerated(EnumType.STRING)
    private Weekdays day;

    @ManyToOne
    @JoinColumn(name = "subject_id", referencedColumnName = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "audience_id", referencedColumnName = "audience_id")
    private Audience audience;

    @Max(value = 91, message = "Duration must be in range of 40 and 90")
    @Min(value = 40, message = "Duration must be in range of 40 and 90")
    @Column(name = "duration")
    private int duration;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public LessonNumber getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(LessonNumber lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public Weekdays getDay() {
        return day;
    }

    public void setDay(Weekdays day) {
        this.day = day;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((audience == null) ? 0 : audience.hashCode());
        result = prime * result + duration;
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
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
        Lesson other = (Lesson) obj;
        if (audience == null) {
            if (other.audience != null)
                return false;
        } else if (!audience.equals(other.audience))
            return false;
        if (duration != other.duration)
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        if (teacher == null) {
            if (other.teacher != null)
                return false;
        } else if (!teacher.equals(other.teacher))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Lesson [subject=" + subject + ", teacher=" + teacher + ", audience=" + audience + ", duration="
                + duration + "]";
    }

}
