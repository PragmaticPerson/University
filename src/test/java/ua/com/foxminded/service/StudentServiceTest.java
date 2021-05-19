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

import ua.com.foxminded.dao.repository.StudentRepository;
import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.people.Student;
import ua.com.foxminded.service.services.StudentService;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class StudentServiceTest {
    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepos;

    @Test
    void getStudentTest() throws ServiceException {
        studentService.get(1);
        verify(studentRepos).getOne(anyInt());
    }

    @Test
    void getAllStudentTest() throws ServiceException {
        studentService.getAll();
        verify(studentRepos).findAll();
    }

    @Test
    void addStudentTest() throws ServiceException {
        studentService.add(new Student());
        verify(studentRepos).save(any());
    }

    @Test
    void deleteStudentTest() throws ServiceException {
        studentService.delete(1);
        verify(studentRepos).softDelete(anyInt());
    }

    @Test
    void changeGroupTest() throws ServiceException {
        studentService.update(new Student());
        verify(studentRepos).save(any());
    }

}
