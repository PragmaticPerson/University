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

import ua.com.foxminded.dao.repository.TeacherRepository;
import ua.com.foxminded.service.models.people.Teacher;
import ua.com.foxminded.service.services.TeacherService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(TeacherRestController.class)
class TeacherRestControllerTest {
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private TeacherRepository teacherRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    void getAllTeachersFromRestTest() throws Exception {
        Teacher teacher = new Teacher(1, "Name", "Surname");
        when(teacherService.getAll()).thenReturn(Arrays.asList(teacher));
        mvc.perform(get("/webapp/teachers")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is(teacher.getName())))
                .andExpect(jsonPath("$[0].surname", is(teacher.getSurname())));
        verify(teacherService).getAll();
    }

    @Test
    void postTeacherToRestTest() throws Exception {
        when(teacherRepository.countByNameAndSurnameAndIdNot("Name", "Surname", 0)).thenReturn(0);
        Teacher teacher = new Teacher(1, "Name", "Surname");
        MockHttpServletRequestBuilder request = post("/webapp/teachers");
        request.header("Content-Type", "application/json");
        request.content("{\"name\":\"Name\",\"surname\":\"Surname\",\"subjects\":[{\"id\":\"1\"},{\"id\":\"3\"}]}");

        mvc.perform(request).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(0)))
                .andExpect(jsonPath("$.name", is(teacher.getName())))
                .andExpect(jsonPath("$.surname", is(teacher.getSurname())))
                .andExpect(jsonPath("$._links.lessons.href", is("/lessons/teachers/0/{day}")));
        verify(teacherService).add(any());
    }

    @Test
    void putTeacherToRestTest() throws Exception {
        when(teacherRepository.countByNameAndSurnameAndIdNot("Name", "Surname", 0)).thenReturn(0);
        Teacher teacher = new Teacher(1, "Name", "Surname");
        MockHttpServletRequestBuilder request = put("/webapp/teachers/1");
        request.header("Content-Type", "application/json");
        request.content("{\"name\":\"Name\",\"surname\":\"Surname\",\"subjects\":[{\"id\":\"1\"},{\"id\":\"3\"}]}");

        mvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(teacher.getId())))
                .andExpect(jsonPath("$.name", is(teacher.getName())))
                .andExpect(jsonPath("$.surname", is(teacher.getSurname())))
                .andExpect(jsonPath("$._links.lessons.href", is("/lessons/teachers/1/{day}")));
        verify(teacherService).update(any());
    }

    @Test
    void getTeacherFromRestTest() throws Exception {
        Teacher teacher = new Teacher(1, "Name", "Surname");
        when(teacherService.get(anyInt())).thenReturn(teacher);
        mvc.perform(get("/webapp/teachers/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(teacher.getId())))
                .andExpect(jsonPath("$.name", is(teacher.getName())))
                .andExpect(jsonPath("$.surname", is(teacher.getSurname())))
                .andExpect(jsonPath("$._links.lessons.href", is("/lessons/teachers/1/{day}")));
        verify(teacherService).get(1);
    }

    @Test
    void deleteTeacherFromRestTest() throws Exception {
        mvc.perform(delete("/webapp/teachers/1")).andExpect(status().is(204));
        verify(teacherService).delete(1);
    }

}
