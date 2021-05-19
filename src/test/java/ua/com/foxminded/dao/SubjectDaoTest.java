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

import ua.com.foxminded.dao.repository.SubjectRepository;
import ua.com.foxminded.service.models.subject.Subject;

@ActiveProfiles("test")
@DataJpaTest
@Import(DataSourceConfiguration.class)
@DatabaseSetup(value = "classpath:data.xml")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestExecutionListeners({ SpringBootDependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
class SubjectDaoTest {
    private SubjectRepository subjectRepos;

    @Autowired
    public SubjectDaoTest(SubjectRepository subjectRepos) {
        this.subjectRepos = subjectRepos;
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/subjects/insertTest.xml")
    void addSubjectTest() {
        subjectRepos.save(new Subject("TestName"));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/subjects/deleteTest.xml")
    void deleteSubjectTest() {
        subjectRepos.softDelete(1);
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/subjects/updateTest.xml")
    void updateSubjectTest() {
        Subject subject = new Subject(5, "NewSubjectName");
        subjectRepos.saveAndFlush(subject);
    }

    @Test
    void getSubjectByIdTest() {
        String expName = "Subject1";
        Subject subject = subjectRepos.getOne(1);
        assertEquals(expName, subject.getName());
    }

    @Test
    void getAllSubjectsTest() {
        List<String> names = Arrays.asList("Subject1", "Subject2", "Subject3", "Subject4", "Subject5", "Subject6",
                "Subject7", "Subject8", "Subject9", "Subject10");

        List<Subject> subjects = subjectRepos.findAll();

        IntStream.range(0, subjects.size()).forEach(i -> {
            assertEquals(names.get(i), subjects.get(i).getName());
        });
    }

    @Test
    void contByNumberAndIdNotTest() {
        int expectedCount = 1;
        int count = subjectRepos.countByNameAndIdNot("Subject1", 0);
        assertEquals(expectedCount, count);
    }

    @Test
    void shouldThrowObjectRetrievalFailureExceptionWhenSubjectWithIdDoesntExist() {
        ObjectRetrievalFailureException exception = assertThrows(ObjectRetrievalFailureException.class, () -> {
            subjectRepos.getOne(100);
        });
        String exceptionMessage = "Unable to find ua.com.foxminded.service.models.subject.Subject with id 100";
        assertEquals(exceptionMessage, exception.getCause().getMessage());
    }
}
