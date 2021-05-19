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

import ua.com.foxminded.dao.repository.GroupRepository;
import ua.com.foxminded.service.models.faculty.Faculty;
import ua.com.foxminded.service.models.faculty.Group;
import ua.com.foxminded.service.services.FacultyService;
import ua.com.foxminded.service.services.GroupService;
import ua.com.foxminded.servlet.controllers.TemplateResolverConfiguration;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(GroupController.class)
@Import(TemplateResolverConfiguration.class)
class GroupControllerTest {
    @MockBean
    private GroupService groupService;
    @MockBean
    private FacultyService facultyService;
    @MockBean
    private GroupRepository groupRepos;

    @InjectMocks
    private GroupController groupsController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addEntityGetRequestTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/groups/add");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("groups/addGroup"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("group"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("faculties"));
    }

    @Test
    void addEntityPostRequestTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/groups/add");
        request.flashAttr("group", new Group(1, "Name", new Faculty(1, "Name", "Name", "Surname")));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/groups/get-all"));
        verify(groupService).add(any());
    }

    @Test
    void getCurrentGetRequestTest() throws Exception {
        List<Faculty> expectedFaculties = Arrays.asList(new Faculty(1, "Test faculty 1", "Name 1", "Surname 1"),
                new Faculty(2, "Test faculty 2", "Name 2", "Surname 1"));
        Group expectedGroup = new Group(1, "Test name", expectedFaculties.get(0));
        when(groupService.get(anyInt())).thenReturn(expectedGroup);
        when(facultyService.getAll()).thenReturn(expectedFaculties);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/groups/get?id=1");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("groups/editGroup"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("group"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("faculties"))
                .andExpect(MockMvcResultMatchers.model().attribute("group", expectedGroup))
                .andExpect(MockMvcResultMatchers.model().attribute("faculties", expectedFaculties));
    }

    @Test
    void getCurrentPostRequestToSaveDataWhenValidationSuccessfulTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/groups/get");
        request.param("save", "");
        request.flashAttr("group", new Group(1, "Name", new Faculty(1, "Name", "Name", "Surname")));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/groups/get-all"));
        verify(groupService).update(any());
    }

    @Test
    void getCurrentPostRequestToSaveDataWhenValidationFailedWhenFacultyEmptyTest() throws Exception {
        String errorMessage = "Faculty cannot be null";

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/groups/get");
        request.param("save", "");
        request.flashAttr("group", new Group(1, "Name", null));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("groups/editGroup"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", errorMessage));
    }
    
    @Test
    void getCurrentPostRequestToSaveDataWhenValidationFailedWhenGroupNameExistsTest() throws Exception {
        String errorMessage = "Group with this name already exists";
        when(groupRepos.countByNameAndIdNot(anyString(), anyInt())).thenReturn(2);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/groups/get");
        request.param("save", "");
        request.flashAttr("group", new Group(1, "Name",  new Faculty(1, "Name", "Name", "Surname")));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("groups/editGroup"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", errorMessage));
    }

    @Test
    void getCurrentPostRequestToDeleteDataTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/groups/get");
        request.param("delete", "");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/groups/get-all"));
        verify(groupService).delete(anyInt());
    }

    @Test
    void getAllGetRequestTest() throws Exception {
        List<Group> expectedGroups = Arrays.asList(new Group(1, "Test name 1", new Faculty()),
                new Group(2, "Test name 2", new Faculty()));
        when(groupService.getAll()).thenReturn(expectedGroups);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/groups/get-all");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("groups/getAllGroups"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("groups"))
                .andExpect(MockMvcResultMatchers.model().attribute("groups", expectedGroups));
    }

    @Test
    void getAllPostRequestTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/groups/get-all");
        request.param("id", "7");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/groups/get-all"));
        verify(groupService).delete(anyInt());
    }
}
