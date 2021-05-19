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

import ua.com.foxminded.dao.repository.SubjectRepository;
import ua.com.foxminded.service.models.subject.Subject;
import ua.com.foxminded.service.services.SubjectService;
import ua.com.foxminded.servlet.controllers.TemplateResolverConfiguration;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(SubjectController.class)
@Import(TemplateResolverConfiguration.class)
class SubjectControllerTest {
    @MockBean
    private SubjectService subjectService;
    @MockBean
    private SubjectRepository subjectRepos;

    @InjectMocks
    private SubjectController subjectsController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addEntityGetRequestTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/subjects/add");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("subjects/addSubject"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("subject"));
    }

    @Test
    void addEntityPostWhenValidationSuccessfulRequestTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/subjects/add");
        request.flashAttr("subject", new Subject(1, "Name"));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/subjects/get-all"));
        verify(subjectService).add(any());
    }

    @Test
    void addEntityPostWhenValidationFailedRequestTest() throws Exception {
        String errorMessage = "Subject with this name is already exists";
        when(subjectRepos.countByNameAndIdNot(anyString(), anyInt())).thenReturn(4);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/subjects/add");
        request.flashAttr("subject", new Subject(1, "Name"));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("subjects/addSubject"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", errorMessage));
    }

    @Test
    void getCurrentGetRequestTest() throws Exception {
        Subject expectedSubject = new Subject(1, "");
        when(subjectService.get(anyInt())).thenReturn(expectedSubject);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/subjects/get?id=1");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("subjects/editSubject"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("subject"))
                .andExpect(MockMvcResultMatchers.model().attribute("subject", expectedSubject));
    }

    @Test
    void getCurrentPostRequestToSaveDataTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/subjects/get");
        request.param("save", "");
        request.flashAttr("subject", new Subject(1, "Name"));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/subjects/get-all"));
        verify(subjectService).update(any());
    }

    @Test
    void getCurrentPostRequestToDeleteDataTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/subjects/get");
        request.param("delete", "");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/subjects/get-all"));
        verify(subjectService).delete(anyInt());
    }

    @Test
    void getAllGetRequestTest() throws Exception {
        List<Subject> expectedSubjects = Arrays.asList(new Subject(1, ""), new Subject(2, ""));
        when(subjectService.getAll()).thenReturn(expectedSubjects);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/subjects/get-all");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("subjects/getAllSubjects"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("subjects"))
                .andExpect(MockMvcResultMatchers.model().attribute("subjects", expectedSubjects));
    }

    @Test
    void getAllPostRequestTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/subjects/get-all");
        request.param("id", "7");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/subjects/get-all"));
        verify(subjectService).delete(anyInt());
    }
}
