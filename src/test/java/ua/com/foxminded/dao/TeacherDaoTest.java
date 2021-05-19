package ua.com.foxminded.dao;

import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import ua.com.foxminded.dao.repository.TeacherRepository;
import ua.com.foxminded.service.models.people.Teacher;
import ua.com.foxminded.service.models.subject.Subject;

@ActiveProfiles("test")
@DataJpaTest
@Import(DataSourceConfiguration.class)
@DatabaseSetup(value = "classpath:data.xml")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestExecutionListeners({ SpringBootDependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
class TeacherDaoTest {
    private TeacherRepository teacherRepos;

    @Autowired
    public TeacherDaoTest(TeacherRepository teacherRepos) {
        this.teacherRepos = teacherRepos;
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/teachers/insertTest.xml")
    void addTeacherTest() {
        teacherRepos.save(new Teacher("TestName", "TestSurname"));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/teachers/deleteTest.xml")
    void deleteTeacherTest() {
        teacherRepos.softDelete(1);
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/teachers/updateTest.xml")
    void updateTeacherTest() {
        Teacher teacher = new Teacher(4, "TestName", "TestSurname");
        teacherRepos.saveAndFlush(teacher);
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/teachers/deleteSubjectTest.xml")
    void deleteSubjectFromTecherTest() {
        teacherRepos.deleteAllSubjectsForTeachers(3);
    }

    @Test
    void getTeacherByIdTest() {
        String expName = "Name1";
        String expSurname = "Surname1";
        List<Subject> subjects = Arrays.asList(new Subject(2, "Subject2"), new Subject(5, "Subject5"),
                new Subject(6, "Subject6"));

        Teacher teacher = teacherRepos.getOne(1);

        assertEquals(expName, teacher.getName());
        assertEquals(expSurname, teacher.getSurname());
        assertEquals(subjects, teacher.getSubjects());
    }

    @Test
    void getAllTeachersTest() {
        List<String> names = Arrays.asList("Name1", "Name2", "Name3", "Name4", "Name5");
        List<String> surnames = Arrays.asList("Surname1", "Surname2", "Surname3", "Surname4", "Surname5");
        List<Subject> teacher1 = Arrays.asList(new Subject(2, "Subject2"), new Subject(5, "Subject5"),
                new Subject(6, "Subject6"));
        List<Subject> teacher2 = Arrays.asList(new Subject(1, "Subject1"), new Subject(3, "Subject3"),
                new Subject(8, "Subject8"), new Subject(10, "Subject10"));
        List<Subject> teacher3 = Arrays.asList(new Subject(1, "Subject1"), new Subject(7, "Subject7"),
                new Subject(9, "Subject9"));
        List<Subject> teacher4 = Arrays.asList(new Subject(1, "Subject1"), new Subject(2, "Subject2"),
                new Subject(6, "Subject6"), new Subject(9, "Subject9"));
        List<Subject> teacher5 = Arrays.asList(new Subject(1, "Subject1"), new Subject(2, "Subject2"),
                new Subject(5, "Subject5"), new Subject(8, "Subject8"), new Subject(10, "Subject10"));
        List<List<Subject>> subjects = Arrays.asList(teacher1, teacher2, teacher3, teacher4, teacher5);

        List<Teacher> teachers = teacherRepos.findAll();

        IntStream.range(0, teachers.size()).forEach(i -> {
            assertEquals(names.get(i), teachers.get(i).getName());
            assertEquals(surnames.get(i), teachers.get(i).getSurname());
            assertEquals(subjects.get(i), teachers.get(i).getSubjects());
        });
    }

    @Test
    void countByNameAndSurnameAndIdNotTest() {
        int expectedCount = 1;
        int count = teacherRepos.countByNameAndSurnameAndIdNot("Name1", "Surname1", 0);
        assertEquals(expectedCount, count);
    }

    @Test
    void shouldThrowObjectRetrievalFailureExceptionWhenGroupWithIdDoesntExist() {
        ObjectRetrievalFailureException exception = assertThrows(ObjectRetrievalFailureException.class, () -> {
            teacherRepos.getOne(100);
        });
        String exceptionMessage = "Unable to find ua.com.foxminded.service.models.people.Teacher with id 100";
        assertEquals(exceptionMessage, exception.getCause().getMessage());
    }

}
