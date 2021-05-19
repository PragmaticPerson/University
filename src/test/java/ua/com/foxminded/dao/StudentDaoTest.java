package ua.com.foxminded.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.SpringBootDependencyInjectionTestExecutionListener;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectRetrievalFailureException;

import ua.com.foxminded.dao.repository.StudentRepository;
import ua.com.foxminded.service.models.faculty.Faculty;
import ua.com.foxminded.service.models.faculty.Group;
import ua.com.foxminded.service.models.people.Student;

@ActiveProfiles("test")
@DataJpaTest
@Import(DataSourceConfiguration.class)
@DatabaseSetup(value = "classpath:data.xml")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestExecutionListeners({ SpringBootDependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
class StudentDaoTest {
    private StudentRepository studentRepos;

    @Autowired
    public StudentDaoTest(StudentRepository studentRepos) {
        this.studentRepos = studentRepos;
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/students/insertTest.xml")
    void addStudentTest() {
        Group group = new Group();
        group.setId(4);
        studentRepos.save(new Student(0, "TestName1", "TestSurname1", group));
        group.setId(1);
        studentRepos.save(new Student(0, "TestName2", "TestSurname2", group));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/students/deleteTest.xml")
    void deleteStudentTest() {
        studentRepos.softDelete(1);
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/students/updateTest.xml")
    void updateTest() {
        Student student = new Student(1, "TestName", "TestSurname", new Group(4, ""));
        studentRepos.saveAndFlush(student);
    }

    @Test
    void getStudentByIdTest() {
        String expName = "Name1";
        String expSurname = "Surname1";
        Faculty expFaculty = new Faculty("FacultyName1", "Name1", "Surname1");
        expFaculty.setId(1);
        Group expGroup = new Group("Group1", expFaculty);
        expGroup.setId(1);

        Student student = studentRepos.getOne(1);

        assertEquals(expName, student.getName());
        assertEquals(expSurname, student.getSurname());
        assertEquals(expGroup, student.getGroup());
    }

    @Test
    void getAllStudentsTest() {
        Faculty faculty1 = new Faculty(1, "FacultyName1", "Name1", "Surname1");
        Faculty faculty2 = new Faculty(2, "FacultyName2", "Name2", "Surname2");

        List<String> names = Arrays.asList("Name1", "Name2", "Name3", "Name4");
        List<String> surnames = Arrays.asList("Surname1", "Surname2", "Surname3", "Surname4");
        List<Group> groups = Arrays.asList(new Group(1, "Group1", faculty1), new Group(1, "Group1", faculty1),
                new Group(2, "Group2", faculty1), new Group(3, "Group3", faculty2));

        List<Student> students = studentRepos.findAll();

        IntStream.range(0, students.size()).forEach(i -> {
            assertEquals(names.get(i), students.get(i).getName());
            assertEquals(surnames.get(i), students.get(i).getSurname());
            assertEquals(groups.get(i), students.get(i).getGroup());
        });
    }

    @Test
    void countStudentsInGroupTest() {
        int expectedCount = 2;
        int count = studentRepos.countByGroupId(1);
        assertEquals(expectedCount, count);
    }

    @Test
    void shouldThrowObjectRetrievalFailureExceptionWhenStudentWithIdDoesntExist() {
        ObjectRetrievalFailureException exception = assertThrows(ObjectRetrievalFailureException.class, () -> {
            studentRepos.getOne(100);
        });
        String exceptionMessage = "Unable to find ua.com.foxminded.service.models.people.Student with id 100";
        assertEquals(exceptionMessage, exception.getCause().getMessage());
    }
}
