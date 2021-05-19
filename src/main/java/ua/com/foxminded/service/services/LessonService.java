package ua.com.foxminded.service.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.dao.repository.LessonRepository;
import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.timetable.Lesson;
import ua.com.foxminded.service.models.timetable.Weekdays;

@Service
public class LessonService {
    private LessonRepository lessonRepos;

    @Autowired
    public LessonService(LessonRepository lessonRepos) {
        this.lessonRepos = lessonRepos;
    }

    public void add(Lesson lesson) throws ServiceException {
        try {
            lessonRepos.save(lesson);
        } catch (RuntimeException e) {
            String message = String.format("Unable to add lesson: %s", lesson);
            throw new ServiceException(message, e);
        }
    }

    public List<Lesson> getLessonsByGroup(int id) throws ServiceException {
        try {
            return lessonRepos.getLessonsByGroup(id);
        } catch (RuntimeException e) {
            String message = String.format("Can't get lesson by group %d", id);
            throw new ServiceException(message, e);
        }
    }

    public List<Lesson> getLessonsByGroupAndDay(int id, Weekdays day) throws ServiceException {
        try {
            return lessonRepos.getLessonsByGroupAndDay(id, day);
        } catch (RuntimeException e) {
            String message = String.format("Can't get lesson by group %d and day %s", id, day);
            throw new ServiceException(message, e);
        }
    }

    public List<Lesson> getLessonsByTeacher(int id) throws ServiceException {
        try {
            return lessonRepos.getLessonsByTeacher(id);
        } catch (RuntimeException e) {
            String message = String.format("Can't get lesson by teacher %d", id);
            throw new ServiceException(message, e);
        }
    }

    public List<Lesson> getLessonsByTeacherAndDay(int id, Weekdays day) throws ServiceException {
        try {
            return lessonRepos.getLessonsByTeacherAndDay(id, day);
        } catch (RuntimeException e) {
            String message = String.format("Can't get lesson by teacher %d and day %s", id, day);
            throw new ServiceException(message, e);
        }
    }

    public List<Lesson> getLessonsByAudienceAndDay(int id, Weekdays day) throws ServiceException {
        try {
            return lessonRepos.getLessonsByAudienceAndDay(id, day);
        } catch (RuntimeException e) {
            String message = String.format("Can't get lesson by audience %d and day %s", id, day);
            throw new ServiceException(message, e);
        }
    }

    public Lesson getLesson(long id) throws ServiceException {
        try {
            return lessonRepos.getOne(id);
        } catch (RuntimeException e) {
            String message;
            if (e.getCause() instanceof EntityNotFoundException) {
                message = String.format("Wrong id %d", id);
                throw new IllegalArgumentException(message, e);
            }
            message = String.format("Can't get lesson with id %d", id);
            throw new ServiceException(message, e);
        }
    }

    public void delete(long id) throws ServiceException {
        try {
            lessonRepos.deleteById(id);
        } catch (RuntimeException e) {
            String message = String.format("Can't delete lesson with id %d", id);
            throw new ServiceException(message, e);
        }
    }

    public void update(Lesson lesson) throws ServiceException {
        try {
            lessonRepos.save(lesson);
        } catch (RuntimeException e) {
            String message = String.format("Unable to update lesson: %s", lesson);
            throw new ServiceException(message, e);
        }
    }
}
