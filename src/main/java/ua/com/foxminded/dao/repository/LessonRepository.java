package ua.com.foxminded.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ua.com.foxminded.service.models.timetable.Lesson;
import ua.com.foxminded.service.models.timetable.Weekdays;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("SELECT l FROM Lesson l WHERE l.group.id=:id ORDER BY l.id ASC")
    List<Lesson> getLessonsByGroup(@Param("id") int id);

    @Query("SELECT l FROM Lesson l WHERE l.group.id=:id AND l.day=:day ORDER BY l.id ASC")
    List<Lesson> getLessonsByGroupAndDay(@Param("id") int id, @Param("day") Weekdays day);

    @Query("SELECT l FROM Lesson l WHERE l.teacher.id=:id ORDER BY l.id ASC")
    List<Lesson> getLessonsByTeacher(@Param("id") int id);

    @Query("SELECT l FROM Lesson l WHERE l.teacher.id=:id AND l.day=:day ORDER BY l.id ASC")
    List<Lesson> getLessonsByTeacherAndDay(@Param("id") int id, @Param("day") Weekdays day);

    @Query("SELECT l FROM Lesson l WHERE l.audience.id=:id AND l.day=:day ORDER BY l.id ASC")
    List<Lesson> getLessonsByAudienceAndDay(@Param("id") int id, @Param("day") Weekdays day);
}
