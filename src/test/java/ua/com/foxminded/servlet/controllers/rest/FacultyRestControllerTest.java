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

import ua.com.foxminded.dao.repository.FacultyRepository;
import ua.com.foxminded.service.models.faculty.Faculty;
import ua.com.foxminded.service.services.FacultyService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(FacultyRestController.class)
class FacultyRestControllerTest {
    @MockBean
    private FacultyService facultyService;
    @MockBean
    private FacultyRepository facultyRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    void getAllFacultiesFromRestTest() throws Exception {
        Faculty faculty = new Faculty(1, "Faculty", "Name", "Surname");
        when(facultyService.getAll()).thenReturn(Arrays.asList(faculty));
        mvc.perform(get("/webapp/faculties")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is(faculty.getName())))
                .andExpect(jsonPath("$[0].deanFirstName", is(faculty.getDeanFirstName())))
                .andExpect(jsonPath("$[0].deanLastName", is(faculty.getDeanLastName())));
        verify(facultyService).getAll();
    }

    @Test
    void postFacultyToRestTest() throws Exception {
        when(facultyRepository.countByNameAndIdNot("Faculty", 0)).thenReturn(0);
        when(facultyRepository.countByDeanFirstNameAndDeanLastNameAndIdNot("Name", "Surname", 0)).thenReturn(0);
        Faculty faculty = new Faculty(1, "Faculty", "Name", "Surname");
        MockHttpServletRequestBuilder request = post("/webapp/faculties");
        request.header("Content-Type", "application/json");
        request.content("{\"name\":\"Faculty\",\"deanFirstName\":\"Name\",\"deanLastName\":\"Surname\"}");

        mvc.perform(request).andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(faculty.getName())))
                .andExpect(jsonPath("$.deanFirstName", is(faculty.getDeanFirstName())))
                .andExpect(jsonPath("$.deanLastName", is(faculty.getDeanLastName())));
        verify(facultyService).add(any());
    }

    @Test
    void putFacultyToRestTest() throws Exception {
        when(facultyRepository.countByNameAndIdNot("Faculty", 0)).thenReturn(0);
        when(facultyRepository.countByDeanFirstNameAndDeanLastNameAndIdNot("Name", "Surname", 0)).thenReturn(0);
        Faculty faculty = new Faculty(1, "Faculty", "Name", "Surname");
        MockHttpServletRequestBuilder request = put("/webapp/faculties/1");
        request.header("Content-Type", "application/json");
        request.content("{\"name\":\"Faculty\",\"deanFirstName\":\"Name\",\"deanLastName\":\"Surname\"}");

        mvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(faculty.getName())))
                .andExpect(jsonPath("$.deanFirstName", is(faculty.getDeanFirstName())))
                .andExpect(jsonPath("$.deanLastName", is(faculty.getDeanLastName())));
        verify(facultyService).update(any());
    }

    @Test
    void getFacultyFromRestTest() throws Exception {
        Faculty faculty = new Faculty(1, "Faculty", "Name", "Surname");
        when(facultyService.get(anyInt())).thenReturn(faculty);
        mvc.perform(get("/webapp/faculties/1")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(faculty.getId()))).andExpect(jsonPath("$.name", is(faculty.getName())))
                .andExpect(jsonPath("$.deanFirstName", is(faculty.getDeanFirstName())))
                .andExpect(jsonPath("$.deanLastName", is(faculty.getDeanLastName())));
        verify(facultyService).get(1);
    }

    @Test
    void deleteFacultyFromRestTest() throws Exception {
        mvc.perform(delete("/webapp/faculties/1")).andExpect(status().is(204));
        verify(facultyService).delete(1);
    }

}
