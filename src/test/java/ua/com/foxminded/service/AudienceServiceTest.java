package ua.com.foxminded.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.dao.repository.AudienceRepository;
import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.audience.Audience;
import ua.com.foxminded.service.services.AudienceService;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class AudienceServiceTest {
    @InjectMocks
    private AudienceService audienceService;

    @Mock
    private AudienceRepository audienceRepos;

    @Test
    void getAudienceTest() throws ServiceException {
        audienceService.get(1);
        verify(audienceRepos).getOne(anyInt());
    }

    @Test
    void getAllAudiencesTest() throws ServiceException {
        audienceService.getAll();
        verify(audienceRepos).findAll();
    }

    @Test
    void addAudienceTest() throws ServiceException {
        audienceService.add(new Audience());
        verify(audienceRepos).save(any());
    }

    @Test
    void deleteAudienceCountCallsTest() throws ServiceException {
        audienceService.delete(1);
        verify(audienceRepos).softDelete(anyInt());
    }

    @Test
    void updateAudienceTest() throws ServiceException {
        audienceService.update(new Audience());
        verify(audienceRepos).save(any());
    }
}
