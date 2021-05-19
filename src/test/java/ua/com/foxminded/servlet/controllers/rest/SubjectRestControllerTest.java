package ua.com.foxminded.servlet.controllers.rest;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
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

import ua.com.foxminded.dao.repository.SubjectRepository;
import ua.com.foxminded.service.models.subject.Subject;
import ua.com.foxminded.service.services.SubjectService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(SubjectRestController.class)
class SubjectRestControllerTest {
    @MockBean
    private SubjectService subjectService;
    @MockBean
    private SubjectRepository subjectRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    void getAllSubjectsFromRestTest() throws Exception {
        Subject subject = new Subject(1, "Subject");
        when(subjectService.getAll()).thenReturn(Arrays.asList(subject));
        mvc.perform(get("/webapp/subjects")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is(subject.getName())));
        verify(subjectService).getAll();
    }

    @Test
    void postSubjectToRestTest() throws Exception {
        when(subjectRepository.countByNameAndIdNot("Subject", 0)).thenReturn(0);
        Subject subject = new Subject(1, "Subject");
        MockHttpServletRequestBuilder request = post("/webapp/subjects");
        request.header("Content-Type", "application/json");
        request.content("{\"name\":\"Subject\"}");

        mvc.perform(request).andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(subject.getName())));
        verify(subjectService).add(any());
    }

    @Test
    void putSubjectToRestTest() throws Exception {
        when(subjectRepository.countByNameAndIdNot("Subject", 0)).thenReturn(0);

        Subject subject = new Subject(1, "Subject");
        MockHttpServletRequestBuilder request = put("/webapp/subjects/1");
        request.header("Content-Type", "application/json");
        request.content("{\"name\":\"Subject\"}");

        mvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(subject.getName())));
        verify(subjectService).update(any());
    }

    @Test
    void getSubjectFromRestTest() throws Exception {
        Subject subject = new Subject(1, "Subject");
        when(subjectService.get(anyInt())).thenReturn(subject);
        mvc.perform(get("/webapp/subjects/1")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(subject.getId()))).andExpect(jsonPath("$.name", is(subject.getName())));
        verify(subjectService).get(1);
    }

    @Test
    void deleteSubjectFromRestTest() throws Exception {
        mvc.perform(delete("/webapp/subjects/1")).andExpect(status().is(204));
        verify(subjectService).delete(1);
    }

}
