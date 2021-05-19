package ua.com.foxminded.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

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
import ua.com.foxminded.service.models.people.Teacher;
import ua.com.foxminded.service.models.subject.Subject;
import ua.com.foxminded.service.services.TeacherService;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class TeacherServiceTest {
    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepos;
    @Mock
    private SubjectRepository subjectRepos;

    @Test
    void getTeacherTest() throws ServiceException {
        teacherService.get(1);
        verify(teacherRepos).getOne(anyInt());
    }

    @Test
    void getAllTeachersTest() throws ServiceException {
        teacherService.getAll();
        verify(teacherRepos).findAll();
    }

    @Test
    void addTeacherTest() throws ServiceException {
        teacherService.add(new Teacher());
        verify(teacherRepos).save(any());
    }

    @Test
    void deleteTeacherCountCallsTest() throws ServiceException {
        teacherService.delete(1);
        verify(teacherRepos).softDelete(anyInt());
    }

    @Test
    void updateTeacher() throws ServiceException {
        List<Subject> subjetcs = Arrays.asList(new Subject(1, "Subj 1"), new Subject(5, "Subj 5"),
                new Subject(8, "Subj 8"));
        Teacher teacherFromBd = new Teacher(1, "Name", "Surname");
        teacherFromBd.setSubjects(subjetcs);
        when(teacherRepos.getOne(anyInt())).thenReturn(teacherFromBd);

        List<Subject> updateSubjects = Arrays.asList(new Subject(1, ""), new Subject(4, ""));
        Teacher teacherToUpdate = new Teacher(1, "Name", "Surname");
        teacherToUpdate.setSubjects(updateSubjects);

        teacherService.update(teacherToUpdate);
        verify(teacherRepos).save(any());
    }

}
