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

import ua.com.foxminded.dao.repository.FacultyRepository;
import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.faculty.Faculty;
import ua.com.foxminded.service.services.FacultyService;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class FacultyServiceTest {
    @InjectMocks
    private FacultyService facultyService;

    @Mock
    private FacultyRepository facultyDao;

    @Test
    void getFacultyTest() throws ServiceException {
        facultyService.get(1);
        verify(facultyDao).getOne(anyInt());
    }

    @Test
    void getAllFacultysTest() throws ServiceException {
        facultyService.getAll();
        verify(facultyDao).findAll();
    }

    @Test
    void addFacultyTest() throws ServiceException {
        facultyService.add(new Faculty());
        verify(facultyDao).save(any());
    }

    @Test
    void deleteTest() throws ServiceException {
        facultyService.delete(1);
        verify(facultyDao).softDelete(anyInt());
    }

    @Test
    void updateTest() throws ServiceException {
        facultyService.update(new Faculty());
        verify(facultyDao).save(any());
    }

}
