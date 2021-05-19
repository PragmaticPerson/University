package ua.com.foxminded.servlet.controllers.rest;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import ua.com.foxminded.dao.repository.AudienceRepository;
import ua.com.foxminded.service.models.audience.Audience;
import ua.com.foxminded.service.services.AudienceService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(AudienceRestController.class)
class AudienceRestControllerTest {
    @MockBean
    private AudienceService audienceService;
    @MockBean
    private AudienceRepository audienceRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    void getAllAudiencesFromRestTest() throws Exception {
        Audience audience = new Audience(1, 101, 40);
        when(audienceService.getAll()).thenReturn(Arrays.asList(audience));
        mvc.perform(get("/webapp/audiences")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].number", is(audience.getNumber())))
                .andExpect(jsonPath("$[0].capacity", is(audience.getCapacity())));
        verify(audienceService).getAll();
    }

    @Test
    void postAudienceToRestTest() throws Exception {
        when(audienceRepository.countByNumberAndIdNot(101, 0)).thenReturn(0);
        Audience audience = new Audience(0, 101, 40);
        MockHttpServletRequestBuilder request = post("/webapp/audiences");
        request.header("Content-Type", "application/json");
        request.content("{\"number\":\"101\",\"capacity\":\"40\"}");

        mvc.perform(request).andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(audience.getId())))
                .andExpect(jsonPath("$.number", is(audience.getNumber())))
                .andExpect(jsonPath("$.capacity", is(audience.getCapacity())));
        verify(audienceService).add(any());
    }

    @Test
    void putAudienceToRestTest() throws Exception {
        when(audienceRepository.countByNumberAndIdNot(101, 1)).thenReturn(0);
        Audience audience = new Audience(1, 101, 40);
        MockHttpServletRequestBuilder request = put("/webapp/audiences/1");
        request.header("Content-Type", "application/json");
        request.content("{\"number\":\"101\",\"capacity\":\"40\"}");

        mvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(audience.getId())))
                .andExpect(jsonPath("$.number", is(audience.getNumber())))
                .andExpect(jsonPath("$.capacity", is(audience.getCapacity())));
        verify(audienceService).update(any());
    }

    @Test
    void getAudienceFromRestTest() throws Exception {
        Audience audience = new Audience(1, 101, 40);
        when(audienceService.get(anyInt())).thenReturn(audience);
        mvc.perform(get("/webapp/audiences/1")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.number", is(audience.getNumber())))
                .andExpect(jsonPath("$.capacity", is(audience.getCapacity())));
        verify(audienceService).get(1);
    }

    @Test
    void deleteAudienceFromRestTest() throws Exception {
        mvc.perform(delete("/webapp/audiences/1")).andExpect(status().is(204));
        verify(audienceService).delete(1);
    }

}
