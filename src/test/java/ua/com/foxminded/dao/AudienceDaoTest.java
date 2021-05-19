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

import ua.com.foxminded.dao.repository.AudienceRepository;
import ua.com.foxminded.service.models.audience.Audience;

@ActiveProfiles("test")
@DataJpaTest
@Import(DataSourceConfiguration.class)
@DatabaseSetup(value = "classpath:data.xml")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestExecutionListeners({ SpringBootDependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
class AudienceDaoTest {
    private AudienceRepository audienceRepos;

    @Autowired
    public AudienceDaoTest(AudienceRepository audienceRepos) {
        this.audienceRepos = audienceRepos;
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/audiences/insertTest.xml")
    void addAudienceTest() {
        audienceRepos.save(new Audience(108, 100));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/audiences/deleteTest.xml")
    void deleteAudienceTest() {
        audienceRepos.softDelete(1);
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "classpath:dbunit/audiences/updateTest.xml")
    void updateAudienceTest() {
        Audience audience = new Audience(111, 90);
        audience.setId(2);
        audienceRepos.saveAndFlush(audience);
    }

    @Test
    void getAudienceByIdTest() {
        int expNumber = 101;
        int expCapacity = 29;

        Audience audience = audienceRepos.getOne(1);

        assertEquals(expNumber, audience.getNumber());
        assertEquals(expCapacity, audience.getCapacity());
    }

    @Test
    void getAllAudiencesTest() {
        List<Integer> numbers = Arrays.asList(101, 102, 103, 104, 105, 106, 107);
        List<Integer> capacities = Arrays.asList(29, 40, 40, 20, 36, 50, 60);

        List<Audience> audiences = audienceRepos.findAll();

        assertFalse(audiences.isEmpty());
        IntStream.range(0, audiences.size()).forEach(i -> {
            assertEquals((int) numbers.get(i), audiences.get(i).getNumber());
            assertEquals((int) capacities.get(i), audiences.get(i).getCapacity());
        });
    }

    @Test
    void contByNumberAndIdNotTest() {
        int expectedCount = 1;
        int count = audienceRepos.countByNumberAndIdNot(102, 0);
        assertEquals(expectedCount, count);
    }

    @Test
    void shouldThrowObjectRetrievalFailureExceptionWhenAudienceWithIdDoesntExist() {
        ObjectRetrievalFailureException exception = assertThrows(ObjectRetrievalFailureException.class, () -> {
            audienceRepos.getOne(100);
        });
        String exceptionMessage = "Unable to find ua.com.foxminded.service.models.audience.Audience with id 100";
        assertEquals(exceptionMessage, exception.getCause().getMessage());
        exception.getCause().printStackTrace();
    }
}
