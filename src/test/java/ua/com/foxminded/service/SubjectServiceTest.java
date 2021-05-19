package ua.com.foxminded.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.dao.repository.SubjectRepository;
import ua.com.foxminded.dao.repository.TeacherRepository;
import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.subject.Subject;
import ua.com.foxminded.service.services.SubjectService;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class SubjectServiceTest {
    @InjectMocks
    private SubjectService subjectService;

    @Mock
    private SubjectRepository subjectRepos;
    @Mock
    private TeacherRepository teacherRepos;

    @Test
    void getSubjectTest() throws ServiceException {
        subjectService.get(1);
        verify(subjectRepos).getOne(anyInt());
    }

    @Test
    void getAllSubjectsTest() throws ServiceException {
        subjectService.getAll();
        verify(subjectRepos).findAll();
    }

    @Test
    void addSubjectTest() throws ServiceException {
        subjectService.add(new Subject());
        verify(subjectRepos).save(any());
    }

    @Test
    void deleteSubjectCountCallsTest() throws ServiceException {
        subjectService.delete(1);
        verify(teacherRepos).deleteAllSubjectsForTeachers(anyInt());
        verify(subjectRepos).softDelete(anyInt());
    }

}
