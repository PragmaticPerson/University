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

import ua.com.foxminded.dao.repository.FacultyRepository;
import ua.com.foxminded.service.models.faculty.Faculty;
import ua.com.foxminded.service.services.FacultyService;
import ua.com.foxminded.servlet.controllers.TemplateResolverConfiguration;
import ua.com.foxminded.validator.FacultyValidator;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(FacultyController.class)
@Import(TemplateResolverConfiguration.class)
class FacultyControllerTest {
    @MockBean
    private FacultyService facultyService;
    @MockBean
    private FacultyRepository facultyRepos;

    @InjectMocks
    private FacultyValidator validator;
    @InjectMocks
    private FacultyController facultyController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addEntityGetRequestTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/faculties/add");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("faculties/addFaculty"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("faculty"));
    }

    @Test
    void addEntityPostRequestWhenValidationSuccessfulTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/faculties/add");
        request.flashAttr("faculty", new Faculty(1, "Name", "Name", "Surname"));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/faculties/get-all"));
        verify(facultyService).add(any());
    }

    @Test
    void addEntityPostRequestWhenValidationFailedWhenNamesAreEmptyTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/faculties/add");
        request.flashAttr("faculty", new Faculty(1, "", "", ""));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("faculties/addFaculty"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"));
    }

    @Test
    void addEntityPostRequestWhenValidationFailedWhenFacultyNameExistsTest() throws Exception {
        String errorMessage = "Faculty with this name already exists";
        when(facultyRepos.countByNameAndIdNot(anyString(), anyInt())).thenReturn(1);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/faculties/add");
        request.flashAttr("faculty", new Faculty(1, "Name", "Name2", "Surname2"));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("faculties/addFaculty"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", errorMessage));
    }

    @Test
    void addEntityPostRequestWhenValidationFailedWhenDeanLeadMoreThenOneFacultyTest() throws Exception {
        String errorMessage = "This dean can't lead more than one faculty";
        when(facultyRepos.countByDeanFirstNameAndDeanLastNameAndIdNot(anyString(), anyString(), anyInt()))
                .thenReturn(1);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/faculties/add");
        request.flashAttr("faculty", new Faculty(1, "Name", "Name2", "Surname2"));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("faculties/addFaculty"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", errorMessage));
    }

    @Test
    void getCurrentGetRequestTest() throws Exception {
        Faculty expectedAudience = new Faculty(1, "Faculty", "Name", "Surname");
        when(facultyService.get(anyInt())).thenReturn(expectedAudience);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/faculties/get?id=1");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("faculties/editFaculty"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("faculty"))
                .andExpect(MockMvcResultMatchers.model().attribute("faculty", expectedAudience));
    }

    @Test
    void getCurrentPostRequestToSaveDataTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/faculties/get");
        request.param("save", "");
        request.flashAttr("faculty", new Faculty(1, "Name", "Name", "Surname"));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/faculties/get-all"));
        verify(facultyService).update(any());
    }

    @Test
    void getCurrentPostRequestToDeleteDataTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/faculties/get");
        request.param("delete", "");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/faculties/get-all"));
        verify(facultyService).delete(anyInt());
    }

    @Test
    void getAllGetRequestTest() throws Exception {
        List<Faculty> expectedFaculties = Arrays.asList(new Faculty(1, "", "", ""), new Faculty(2, "", "", ""));
        when(facultyService.getAll()).thenReturn(expectedFaculties);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/faculties/get-all");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("faculties/getAllFaculties"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("faculties"))
                .andExpect(MockMvcResultMatchers.model().attribute("faculties", expectedFaculties));
    }

    @Test
    void getAllPostRequestTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/faculties/get-all");
        request.param("id", "7");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/faculties/get-all"));
        verify(facultyService).delete(anyInt());
    }
}
