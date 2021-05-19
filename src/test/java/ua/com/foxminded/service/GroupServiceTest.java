package ua.com.foxminded.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.dao.repository.GroupRepository;
import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.faculty.Group;
import ua.com.foxminded.service.services.GroupService;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class GroupServiceTest {
    @InjectMocks
    private GroupService groupService;

    @Mock
    private GroupRepository groupRepos;

    @Test
    void getGroupTest() throws ServiceException {
        groupService.get(1);
        verify(groupRepos).getOne(anyInt());
    }

    @Test
    void getAllGroupsTest() throws ServiceException {
        groupService.getAll();
        verify(groupRepos).findAll();
    }

    @Test
    void addGroupTest() throws ServiceException {
        groupService.add(new Group());
        verify(groupRepos).save(any());
    }

    @Test
    void deleteGroupCountCallsTest() throws ServiceException {
        groupService.delete(1);
        verify(groupRepos).softDelete(anyInt());
    }

    @Test
    void updateGroupTest() throws ServiceException {
        groupService.update(new Group());
        verify(groupRepos).save(any());
    }
}
