package ua.com.foxminded.servlet.controllers.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ua.com.foxminded.dao.repository.StudentRepository;
import ua.com.foxminded.service.models.faculty.Group;
import ua.com.foxminded.service.models.people.Student;
import ua.com.foxminded.service.services.GroupService;
import ua.com.foxminded.service.services.StudentService;
import ua.com.foxminded.servlet.controllers.TemplateResolverConfiguration;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(StudentController.class)
@Import(TemplateResolverConfiguration.class)
class StudentControllerTest {
    @MockBean
    private StudentService studentService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private StudentRepository studentRepos;

    @InjectMocks
    private StudentController studentsController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addEntityGetRequestTest() throws Exception {
        List<Group> groups = Arrays.asList(new Group(), new Group());
        when(groupService.getAll()).thenReturn(groups);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/students/add");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("students/addStudent"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("student"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("groups"))
                .andExpect(MockMvcResultMatchers.model().attribute("groups", groups));
    }

    @Test
    void addEntityPostRequestWhenValidationSuccessfulTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/students/add");
        request.flashAttr("student", new Student(1, "Name", "Surname", new Group(1, "Name")));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/students/get-all"));
        verify(studentService).add(any());
    }

    @Test
    void addEntityPostRequestWhenValidationFailedWhenGroupIsFullTest() throws Exception {
        String errorMessage = "There is no empty space in group. Please, choose another group";
        when(studentRepos.countByGroupId(anyInt())).thenReturn(40);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/students/add");
        request.flashAttr("student", new Student(1, "Name", "Surname", new Group(1, "Name")));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("students/addStudent"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", errorMessage));
    }

    @Test
    void getCurrentGetRequestTest() throws Exception {
        Student expectedStudent = new Student(0, "", "", new Group());
        when(studentService.get(anyInt())).thenReturn(expectedStudent);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/students/get?id=1");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("students/editStudent"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("student"))
                .andExpect(MockMvcResultMatchers.model().attribute("student", expectedStudent));
    }

    @Test
    void getCurrentPostRequestToSaveDataTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/students/get");
        request.param("save", "");
        request.flashAttr("student", new Student(1, "Name", "Surname", new Group(1, "Name")));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/students/get-all"));
        verify(studentService).update(any());
    }

    @Test
    void getAllGetRequestTest() throws Exception {
        List<Student> expectedStudents = Arrays.asList(new Student(1, "Name 1", "Surname 1", new Group()),
                new Student(2, "Name 2", "Surname 2", new Group()));
        when(studentService.getAll()).thenReturn(expectedStudents);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/students/get-all");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("students/getAllStudents"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("students"))
                .andExpect(MockMvcResultMatchers.model().attribute("students", expectedStudents));
    }

    @Test
    void getAllPostRequestTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/students/get-all");
        request.param("id", "7");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/students/get-all"));
        verify(studentService).delete(anyInt());
    }
}
