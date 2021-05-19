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

import ua.com.foxminded.dao.repository.GroupRepository;
import ua.com.foxminded.service.models.faculty.Faculty;
import ua.com.foxminded.service.models.faculty.Group;

@ActiveProfiles("test")
@DataJpaTest
@Import(DataSourceConfiguration.class)
@DatabaseSetup(value = "classpath:data.xml")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestExecutionListeners({ SpringBootDependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
class GroupDaoTest {
    private GroupRepository groupRepos;

    @Autowired
    public GroupDaoTest(GroupRepository groupRepos) {
        this.groupRepos = groupRepos;
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/groups/insertTest.xml")
    void addGroupTest() {
        Faculty faculty = new Faculty();
        faculty.setId(1);
        groupRepos.save(new Group("TestName1", faculty));
        faculty.setId(2);
        groupRepos.save(new Group("TestName2", faculty));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/groups/deleteTest.xml")
    void deleteGroupsTest() {
        groupRepos.softDelete(1);
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:dbunit/groups/updateTest.xml")
    void updateGroupTest() {
        Faculty faculty = new Faculty(1, "", "", "");
        Group group = new Group(3, "NewName", faculty);
        groupRepos.saveAndFlush(group);
    }

    @Test
    void getGroupByIdTest() {
        String expName = "Group1";
        int expFacultyId = 1;
        String expFacultyName = "FacultyName1";
        String expDeanFirstName = "Name1";
        String expDeanLastName = "Surname1";

        Group group = groupRepos.getOne(1);

        assertEquals(expName, group.getName());
        assertEquals(expFacultyId, group.getFaculty().getId());
        assertEquals(expFacultyName, group.getFaculty().getName());
        assertEquals(expDeanFirstName, group.getFaculty().getDeanFirstName());
        assertEquals(expDeanLastName, group.getFaculty().getDeanLastName());
    }

    @Test
    void getAllGroupsTest() {
        List<String> expNames = Arrays.asList("Group1", "Group2", "Group3", "Group4");
        List<Integer> expFacultyIds = Arrays.asList(1, 1, 2, 2);
        List<String> expFacultyNames = Arrays.asList("FacultyName1", "FacultyName1", "FacultyName2", "FacultyName2");
        List<String> expDeanFirstNames = Arrays.asList("Name1", "Name1", "Name2", "Name2");
        List<String> expDeanLastNames = Arrays.asList("Surname1", "Surname1", "Surname2", "Surname2");

        List<Group> groups = groupRepos.findAll();

        IntStream.range(0, groups.size()).forEach(i -> {
            assertEquals(expNames.get(i), groups.get(i).getName());
            assertEquals((int) expFacultyIds.get(i), groups.get(i).getFaculty().getId());
            assertEquals(expFacultyNames.get(i), groups.get(i).getFaculty().getName());
            assertEquals(expDeanFirstNames.get(i), groups.get(i).getFaculty().getDeanFirstName());
            assertEquals(expDeanLastNames.get(i), groups.get(i).getFaculty().getDeanLastName());
        });
    }

    @Test
    void countByNameAndIdNotTest() {
        int expectedCount = 1;
        int count = groupRepos.countByNameAndIdNot("Group1", 0);
        assertEquals(expectedCount, count);
    }

    @Test
    void shouldThrowObjectRetrievalFailureExceptionWhenGroupWithIdDoesntExist() {
        ObjectRetrievalFailureException exception = assertThrows(ObjectRetrievalFailureException.class, () -> {
            groupRepos.getOne(100);
        });
        String exceptionMessage = "Unable to find ua.com.foxminded.service.models.faculty.Group with id 100";
        assertEquals(exceptionMessage, exception.getCause().getMessage());
    }
}
