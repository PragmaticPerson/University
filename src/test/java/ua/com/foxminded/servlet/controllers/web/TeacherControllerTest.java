package ua.com.foxminded.servlet.controllers.web;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ua.com.foxminded.dao.repository.TeacherRepository;
import ua.com.foxminded.service.models.people.Teacher;
import ua.com.foxminded.service.models.subject.Subject;
import ua.com.foxminded.service.services.SubjectService;
import ua.com.foxminded.service.services.TeacherService;
import ua.com.foxminded.servlet.controllers.TemplateResolverConfiguration;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(TeacherController.class)
@Import(TemplateResolverConfiguration.class)
class TeacherControllerTest {
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private SubjectService subjectService;
    @MockBean
    private TeacherRepository teacherRepos;

    @InjectMocks
    private TeacherController teacherController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addEntityGetRequestTest() throws Exception {
        List<Subject> expectedSubjects = Arrays.asList(new Subject(1, ""), new Subject(2, ""));
        when(subjectService.getAll()).thenReturn(expectedSubjects);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/teachers/add");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("teachers/addTeacher"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("teacher"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allSubjects"))
                .andExpect(MockMvcResultMatchers.model().attribute("allSubjects", expectedSubjects));
    }

    @Test
    void addEntityPostRequestWhenValidationSuccessfulTest() throws Exception {
        List<Subject> subjects = Arrays.asList(new Subject(1, "Name1"));
        Teacher teacher = new Teacher(0, "Name", "Surname");
        teacher.setSubjects(subjects);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/teachers/add");
        request.flashAttr("teacher", teacher);
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/teachers/get-all"));
        verify(teacherService).add(any());
    }

    @Test
    void addEntityPostRequestWhenValidationFailedWhenTeacherWithNameExistsTest() throws Exception {
        String errorMessage = "Teacher with current name and surname already exists.";

        List<Subject> subjects = Arrays.asList(new Subject(1, "Name1"));
        Teacher teacher = new Teacher(0, "Name", "Surname");
        teacher.setSubjects(subjects);

        when(teacherRepos.countByNameAndSurnameAndIdNot(anyString(), anyString(), anyInt())).thenReturn(3);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/teachers/add");
        request.flashAttr("teacher", teacher);
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("teachers/addTeacher"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", errorMessage));
    }

    @Test
    void getCurrentGetRequestTest() throws Exception {
        List<Subject> expectedSubjects = Arrays.asList(new Subject(1, ""), new Subject(2, ""));
        Teacher expectedTeacher = new Teacher(1, "", "");
        when(teacherService.get(anyInt())).thenReturn(expectedTeacher);
        when(subjectService.getAll()).thenReturn(expectedSubjects);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/teachers/get?id=1");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("teachers/editTeacher"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("teacher"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allSubjects"))
                .andExpect(MockMvcResultMatchers.model().attribute("teacher", expectedTeacher))
                .andExpect(MockMvcResultMatchers.model().attribute("allSubjects", expectedSubjects));
        verify(teacherService).get(anyInt());
        verify(subjectService).getAll();
    }

    @Test
    void getCurrentPostRequestToSaveDataTest() throws Exception {
        List<Subject> subjects = Arrays.asList(new Subject(1, "Name1"));
        Teacher teacher = new Teacher(0, "Name", "Surname");
        teacher.setSubjects(subjects);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/teachers/get");
        request.param("save", "");
        request.flashAttr("teacher", teacher);
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/teachers/get-all"));
        verify(teacherService).update(any());
    }

    @Test
    void getCurrentPostRequestToDeleteDataTest() throws Exception {
        List<Subject> subjects = Arrays.asList(new Subject(1, "Name1"));
        Teacher teacher = new Teacher(0, "Name", "Surname");
        teacher.setSubjects(subjects);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/teachers/get");
        request.param("delete", "");
        request.flashAttr("teacher", teacher);
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/teachers/get-all"));
        verify(teacherService).delete(anyInt());
    }

    @Test
    void getAllGetRequestTest() throws Exception {
        List<Teacher> expectedStudents = Arrays.asList(new Teacher(1, "Name 1", "Surname 1"),
                new Teacher(2, "Name 2", "Surname 2"));
        when(teacherService.getAll()).thenReturn(expectedStudents);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/teachers/get-all");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("teachers/getAllTeachers"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("teachers"))
                .andExpect(MockMvcResultMatchers.model().attribute("teachers", expectedStudents));
    }

    @Test
    void getAllPostRequestTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/teachers/get-all");
        request.param("teacherId", "7");
        request.param("subjectId", "2");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/teachers/get-all"));
        verify(teacherService).deleteSubject(anyInt(), anyInt());
    }
}
