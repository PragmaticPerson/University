package ua.com.foxminded.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.SpringBootDependencyInjectionTestExecutionListener;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import ua.com.foxminded.dao.repository.LessonRepository;
import ua.com.foxminded.service.models.audience.Audience;
import ua.com.foxminded.service.models.faculty.Faculty;
import ua.com.foxminded.service.models.faculty.Group;
import ua.com.foxminded.service.models.people.Teacher;
import ua.com.foxminded.service.models.subject.Subject;
import ua.com.foxminded.service.models.timetable.Lesson;
import ua.com.foxminded.service.models.timetable.LessonNumber;
import ua.com.foxminded.service.models.timetable.Weekdays;

@ActiveProfiles("test")
@DataJpaTest
@Import(DataSourceConfiguration.class)
@DatabaseSetup(value = "classpath:data.xml")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestExecutionListeners({ SpringBootDependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
class LessonDaoTest {
    private LessonRepository lessonRepos;

    @Autowired
    public LessonDaoTest(LessonRepository lessonRepos) {
        this.lessonRepos = lessonRepos;
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/lessons/insertTest.xml")
    void addLessonTest() {
        Lesson lesson = new Lesson();
        lesson.setAudience(new Audience(5, 0, 0));
        lesson.setDay(Weekdays.THURSDAY);
        lesson.setDuration(90);
        lesson.setLessonNumber(LessonNumber.FIRST);
        lesson.setSubject(new Subject(5, ""));
        lesson.setTeacher(new Teacher(1, "", ""));
        lesson.setGroup(new Group(4, ""));

        lessonRepos.save(lesson);
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/lessons/deleteTest.xml")
    void deleteLessonTest() {
        lessonRepos.deleteById(5L);
        lessonRepos.flush();
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/lessons/updateTest.xml")
    void updateLessonTest() {
        Lesson lesson = new Lesson();
        lesson.setId(5L);
        lesson.setGroup(new Group(3, ""));
        lesson.setDay(Weekdays.TUESDAY);
        lesson.setLessonNumber(LessonNumber.FOURTH);
        lesson.setAudience(new Audience(3, 0, 0));
        lesson.setTeacher(new Teacher(4, "", ""));
        lesson.setDuration(90);
        lesson.setSubject(new Subject(9, ""));

        lessonRepos.saveAndFlush(lesson);
    }

    @Test
    void getLessonTest() {
        Weekdays MONDAY = Weekdays.MONDAY;
        LessonNumber FIRST = LessonNumber.FIRST;

        Group group = new Group(1, "Group1");
        group.setFaculty(new Faculty(1, "FacultyName1", "Name1", "Surname1"));

        Subject subject = new Subject(2, "Subject2");

        Teacher teacher = new Teacher(1, "Name1", "Surname1");
        teacher.setSubjects(Arrays.asList(subject, new Subject(5, "Subject5"), new Subject(6, "Subject6")));

        Audience audience = new Audience(1, 101, 29);

        Lesson expectedLesson = new Lesson();
        expectedLesson.setAudience(audience);
        expectedLesson.setDay(MONDAY);
        expectedLesson.setLessonNumber(FIRST);
        expectedLesson.setTeacher(teacher);
        expectedLesson.setDuration(90);
        expectedLesson.setGroup(group);
        expectedLesson.setSubject(subject);

        Lesson lesson = lessonRepos.getOne(1L);

        assertEquals(expectedLesson, lesson);
    }

    @Test
    void getLessonsForGroupByIdTest() {
        List<Integer> teachers = Arrays.asList(1, 4, 1, 5);
        List<Weekdays> days = Arrays.asList(Weekdays.MONDAY, Weekdays.MONDAY, Weekdays.WEDNESDAY, Weekdays.WEDNESDAY);
        List<LessonNumber> numbers = Arrays.asList(LessonNumber.FIRST, LessonNumber.SECOND, LessonNumber.FIRST,
                LessonNumber.FOURTH);
        List<Integer> durations = Arrays.asList(90, 90, 60, 90);
        List<Integer> audiencies = Arrays.asList(1, 1, 1, 2);
        List<Integer> subjects = Arrays.asList(2, 1, 2, 1);

        List<Lesson> lessons = lessonRepos.getLessonsByGroup(1);

        IntStream.range(0, lessons.size()).forEach(i -> {
            assertEquals((int) teachers.get(i), lessons.get(i).getTeacher().getId());
            assertEquals(days.get(i), lessons.get(i).getDay());
            assertEquals(numbers.get(i), lessons.get(i).getLessonNumber());
            assertEquals((int) durations.get(i), lessons.get(i).getDuration());
            assertEquals((int) audiencies.get(i), lessons.get(i).getAudience().getId());
            assertEquals((int) subjects.get(i), lessons.get(i).getSubject().getId());
        });
    }

    @Test
    void getLessonsForGroupByIdAndDayTest() {
        List<Integer> teachers = Arrays.asList(1, 4);
        List<Weekdays> days = Arrays.asList(Weekdays.MONDAY, Weekdays.MONDAY);
        List<LessonNumber> numbers = Arrays.asList(LessonNumber.FIRST, LessonNumber.SECOND);
        List<Integer> durations = Arrays.asList(90, 90);
        List<Integer> audiencies = Arrays.asList(1, 1);
        List<Integer> subjects = Arrays.asList(2, 1);

        List<Lesson> lessons = lessonRepos.getLessonsByGroupAndDay(1, Weekdays.MONDAY);

        IntStream.range(0, lessons.size()).forEach(i -> {
            assertEquals((int) teachers.get(i), lessons.get(i).getTeacher().getId());
            assertEquals(days.get(i), lessons.get(i).getDay());
            assertEquals(numbers.get(i), lessons.get(i).getLessonNumber());
            assertEquals((int) durations.get(i), lessons.get(i).getDuration());
            assertEquals((int) audiencies.get(i), lessons.get(i).getAudience().getId());
            assertEquals((int) subjects.get(i), lessons.get(i).getSubject().getId());
        });
    }

    @Test
    void getLessonsForTeacherByIdTest() {
        List<Integer> groups = Arrays.asList(1, 3);
        List<Weekdays> days = Arrays.asList(Weekdays.MONDAY, Weekdays.WEDNESDAY);
        List<LessonNumber> numbers = Arrays.asList(LessonNumber.SECOND, LessonNumber.FIRST);
        List<Integer> durations = Arrays.asList(90, 60);
        List<Integer> audiencies = Arrays.asList(1, 4);
        List<Integer> subjects = Arrays.asList(1, 6);

        List<Lesson> lessons = lessonRepos.getLessonsByTeacher(4);

        IntStream.range(0, lessons.size()).forEach(i -> {
            assertEquals((int) groups.get(i), lessons.get(i).getGroup().getId());
            assertEquals(days.get(i), lessons.get(i).getDay());
            assertEquals(numbers.get(i), lessons.get(i).getLessonNumber());
            assertEquals((int) durations.get(i), lessons.get(i).getDuration());
            assertEquals((int) audiencies.get(i), lessons.get(i).getAudience().getId());
            assertEquals((int) subjects.get(i), lessons.get(i).getSubject().getId());
        });
    }

    @Test
    void getLessonsForTeacherByIdAndDayTest() {
        List<Integer> groups = Arrays.asList(1);
        List<Weekdays> days = Arrays.asList(Weekdays.MONDAY);
        List<LessonNumber> numbers = Arrays.asList(LessonNumber.SECOND);
        List<Integer> durations = Arrays.asList(90);
        List<Integer> audiencies = Arrays.asList(1);
        List<Integer> subjects = Arrays.asList(1);

        List<Lesson> lessons = lessonRepos.getLessonsByTeacherAndDay(4, Weekdays.MONDAY);

        IntStream.range(0, lessons.size()).forEach(i -> {
            assertEquals((int) groups.get(i), lessons.get(i).getGroup().getId());
            assertEquals(days.get(i), lessons.get(i).getDay());
            assertEquals(numbers.get(i), lessons.get(i).getLessonNumber());
            assertEquals((int) durations.get(i), lessons.get(i).getDuration());
            assertEquals((int) audiencies.get(i), lessons.get(i).getAudience().getId());
            assertEquals((int) subjects.get(i), lessons.get(i).getSubject().getId());
        });
    }

    @Test
    void getLessonsForAudienceByIdAndDayTest() {
        int group = 1;
        Weekdays MONDAY = Weekdays.MONDAY;
        List<LessonNumber> numbers = Arrays.asList(LessonNumber.FIRST, LessonNumber.SECOND);
        int duration = 90;
        List<Integer> teachers = Arrays.asList(1, 4);
        List<Integer> subjects = Arrays.asList(2, 1);

        List<Lesson> lessons = lessonRepos.getLessonsByAudienceAndDay(1, Weekdays.MONDAY);

        IntStream.range(0, lessons.size()).forEach(i -> {
            assertEquals(group, lessons.get(i).getGroup().getId());
            assertEquals(MONDAY, lessons.get(i).getDay());
            assertEquals(numbers.get(i), lessons.get(i).getLessonNumber());
            assertEquals(duration, lessons.get(i).getDuration());
            assertEquals((int) teachers.get(i), lessons.get(i).getTeacher().getId());
            assertEquals((int) subjects.get(i), lessons.get(i).getSubject().getId());
        });
    }
}