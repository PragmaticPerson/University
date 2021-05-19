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

import ua.com.foxminded.dao.repository.FacultyRepository;
import ua.com.foxminded.service.models.faculty.Faculty;

@ActiveProfiles("test")
@DataJpaTest
@Import(DataSourceConfiguration.class)
@DatabaseSetup(value = "classpath:data.xml")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestExecutionListeners({ SpringBootDependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
class FacultyDaoTest {
    private FacultyRepository facultyRepos;

    @Autowired
    public FacultyDaoTest(FacultyRepository facultyRepos) {
        this.facultyRepos = facultyRepos;
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/faculty/insertTest.xml")
    void addFacultyTest() {
        facultyRepos.save(new Faculty("TestFacultyName", "TestName", "TestSurname"));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/faculty/deleteTest.xml")
    void deleteFacultyTest() {
        facultyRepos.softDelete(1);
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/faculty/updateTest.xml")
    void updateFacultyTest() {
        Faculty faculty = new Faculty("ChangedFacultyName", "ChangedName", "ChangedSurname");
        faculty.setId(1);
        facultyRepos.saveAndFlush(faculty);
    }

    @Test
    void getFacultyByIdTest() {
        String expName = "FacultyName1";
        String expDeanFirstName = "Name1";
        String expDeanLastName = "Surname1";

        Faculty faculty = facultyRepos.getOne(1);

        assertEquals(expName, faculty.getName());
        assertEquals(expDeanFirstName, faculty.getDeanFirstName());
        assertEquals(expDeanLastName, faculty.getDeanLastName());
    }

    @Test
    void getAllFacultiesTest() {
        List<String> names = Arrays.asList("FacultyName1", "FacultyName2");
        List<String> deanFirstNames = Arrays.asList("Name1", "Name2");
        List<String> deanLastNames = Arrays.asList("Surname1", "Surname2");

        List<Faculty> faculties = facultyRepos.findAll();

        IntStream.range(0, faculties.size()).forEach(i -> {
            assertEquals(names.get(i), faculties.get(i).getName());
            assertEquals(deanFirstNames.get(i), faculties.get(i).getDeanFirstName());
            assertEquals(deanLastNames.get(i), faculties.get(i).getDeanLastName());
        });
    }

    @Test
    void countByNameAndIdNotTest() {
        int expectedCount = 1;
        int count = facultyRepos.countByNameAndIdNot("FacultyName1", 0);
        assertEquals(expectedCount, count);
    }

    @Test
    void shouldThrowObjectRetrievalFailureExceptionWhenFacultyWithIdDoesntExist() {
        ObjectRetrievalFailureException exception = assertThrows(ObjectRetrievalFailureException.class, () -> {
            facultyRepos.getOne(100);
        });
        String exceptionMessage = "Unable to find ua.com.foxminded.service.models.faculty.Faculty with id 100";
        assertEquals(exceptionMessage, exception.getCause().getMessage());
    }
}
