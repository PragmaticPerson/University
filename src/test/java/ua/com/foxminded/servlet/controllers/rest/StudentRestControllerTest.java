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

import ua.com.foxminded.dao.repository.StudentRepository;
import ua.com.foxminded.service.models.faculty.Group;
import ua.com.foxminded.service.models.people.Student;
import ua.com.foxminded.service.services.StudentService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(StudentRestController.class)
class StudentRestControllerTest {
    @MockBean
    private StudentService studentService;
    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    void getAllStudentsFromRestTest() throws Exception {
        Student student = new Student(1, "Name", "Surname", new Group(1, ""));
        when(studentService.getAll()).thenReturn(Arrays.asList(student));
        mvc.perform(get("/webapp/students")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is(student.getName())))
                .andExpect(jsonPath("$[0].surname", is(student.getSurname())));
        verify(studentService).getAll();
    }

    @Test
    void postStudentToRestTest() throws Exception {
        when(studentRepository.countByGroupId(1)).thenReturn(0);

        Student student = new Student(1, "Name", "Surname", new Group(1, ""));
        MockHttpServletRequestBuilder request = post("/webapp/students");
        request.header("Content-Type", "application/json");
        request.content("{\"name\":\"Name\",\"surname\":\"Surname\",\"group\":{\"id\":\"1\"}}");

        mvc.perform(request).andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(student.getName())))
                .andExpect(jsonPath("$.surname", is(student.getSurname())))
                .andExpect(jsonPath("$.group.id", is(student.getGroup().getId())));
        verify(studentService).add(any());
    }

    @Test
    void putStudentToRestTest() throws Exception {
        when(studentRepository.countByGroupId(1)).thenReturn(0);

        Student student = new Student(1, "Name", "Surname", new Group(1, ""));
        MockHttpServletRequestBuilder request = put("/webapp/students/1");
        request.header("Content-Type", "application/json");
        request.content("{\"name\":\"Name\",\"surname\":\"Surname\",\"group\":{\"id\":\"1\"}}");

        mvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(student.getName())))
                .andExpect(jsonPath("$.surname", is(student.getSurname())))
                .andExpect(jsonPath("$.group.id", is(student.getGroup().getId())));
        verify(studentService).update(any());
    }

    @Test
    void getStudentFromRestTest() throws Exception {
        Student student = new Student(1, "Name", "Surname", new Group(1, ""));

        when(studentService.get(anyInt())).thenReturn(student);
        mvc.perform(get("/webapp/students/1")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(student.getId()))).andExpect(jsonPath("$.name", is(student.getName())))
                .andExpect(jsonPath("$.surname", is(student.getSurname())))
                .andExpect(jsonPath("$.group.id", is(student.getGroup().getId())));
        verify(studentService).get(1);
    }

    @Test
    void deleteStudentFromRestTest() throws Exception {
        mvc.perform(delete("/webapp/students/1")).andExpect(status().is(204));
        verify(studentService).delete(1);
    }

}
