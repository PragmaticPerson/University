package ua.com.foxminded.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.dao.repository.LessonRepository;
import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.timetable.Lesson;
import ua.com.foxminded.service.models.timetable.Weekdays;
import ua.com.foxminded.service.services.LessonService;
import ua.com.foxminded.service.services.TeacherService;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class LessonServiceTest {
    @InjectMocks
    private LessonService lessonService;

    @Mock
    private LessonRepository lessonRepos;
    @Mock
    private TeacherService teacherService;

    @Test
    void addLessontest() throws ServiceException {
        lessonService.add(new Lesson());
        verify(lessonRepos).save(any());
    }

    @Test
    void getLessonsByGroupTest() throws ServiceException {
        lessonService.getLessonsByGroup(1);
        verify(lessonRepos).getLessonsByGroup(anyInt());
    }

    @Test
    void getLessonsByGroupAndDayTest() throws ServiceException {
        lessonService.getLessonsByGroupAndDay(1, Weekdays.FRIDAY);
        verify(lessonRepos).getLessonsByGroupAndDay(anyInt(), any());
    }

    @Test
    void getLessonsByTeacherTest() throws ServiceException {
        lessonService.getLessonsByTeacher(1);
        verify(lessonRepos).getLessonsByTeacher(anyInt());
    }

    @Test
    void getLessonsByTeacherAndDayTest() throws ServiceException {
        lessonService.getLessonsByTeacherAndDay(1, Weekdays.FRIDAY);
        verify(lessonRepos).getLessonsByTeacherAndDay(anyInt(), any());
    }

    @Test
    void removeLessonTest() throws ServiceException {
        lessonService.delete(1);
        verify(lessonRepos).deleteById(anyLong());
    }

    @Test
    void changeLessonTimeTest() throws ServiceException {
        lessonService.update(new Lesson());
        verify(lessonRepos).save(any());
    }

}
