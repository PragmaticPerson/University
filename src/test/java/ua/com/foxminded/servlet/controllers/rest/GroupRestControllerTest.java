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

import ua.com.foxminded.dao.repository.GroupRepository;
import ua.com.foxminded.service.models.faculty.Faculty;
import ua.com.foxminded.service.models.faculty.Group;
import ua.com.foxminded.service.services.GroupService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(GroupRestController.class)
class GroupRestControllerTest {
    @MockBean
    private GroupService groupService;
    @MockBean
    private GroupRepository groupRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    void getAllGroupsFromRestTest() throws Exception {
        Group group = new Group(1, "Group", new Faculty(1, "Faculty", "Name", "Surname"));
        when(groupService.getAll()).thenReturn(Arrays.asList(group));
        mvc.perform(get("/webapp/groups")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is(group.getName())))
                .andExpect(jsonPath("$[0].faculty.name", is(group.getFaculty().getName())))
                .andExpect(jsonPath("$[0].faculty.deanFirstName", is(group.getFaculty().getDeanFirstName())))
                .andExpect(jsonPath("$[0].faculty.deanLastName", is(group.getFaculty().getDeanLastName())));
        verify(groupService).getAll();
    }

    @Test
    void postGroupToRestTest() throws Exception {
        when(groupRepository.countByNameAndIdNot("Group", 0)).thenReturn(0);
        Group group = new Group(0, "Group", new Faculty(1, "Faculty", "Name", "Surname"));
        MockHttpServletRequestBuilder request = post("/webapp/groups");
        request.header("Content-Type", "application/json");
        request.content(
                "{\"name\":\"Group\",\"faculty\":{\"name\":\"Faculty\",\"deanFirstName\":\"Name\",\"deanLastName\":\"Surname\"}}");

        mvc.perform(request).andExpect(status().isCreated()).andExpect(jsonPath("$.id", is(group.getId())))
                .andExpect(jsonPath("$.name", is(group.getName())))
                .andExpect(jsonPath("$.faculty.name", is(group.getFaculty().getName())))
                .andExpect(jsonPath("$.faculty.deanFirstName", is(group.getFaculty().getDeanFirstName())))
                .andExpect(jsonPath("$.faculty.deanLastName", is(group.getFaculty().getDeanLastName())))
                .andExpect(jsonPath("$._links.lessons.href", is("/lessons/groups/0/{day}")));
        verify(groupService).add(any());
    }

    @Test
    void putGroupToRestTest() throws Exception {
        when(groupRepository.countByNameAndIdNot("Group", 0)).thenReturn(0);
        Group group = new Group(1, "Group", new Faculty(1, "Faculty", "Name", "Surname"));
        MockHttpServletRequestBuilder request = put("/webapp/groups/1");
        request.header("Content-Type", "application/json");
        request.content(
                "{\"name\":\"Group\",\"faculty\":{\"name\":\"Faculty\",\"deanFirstName\":\"Name\",\"deanLastName\":\"Surname\"}}");

        mvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(group.getId())))
                .andExpect(jsonPath("$.name", is(group.getName())))
                .andExpect(jsonPath("$.faculty.name", is(group.getFaculty().getName())))
                .andExpect(jsonPath("$.faculty.deanFirstName", is(group.getFaculty().getDeanFirstName())))
                .andExpect(jsonPath("$.faculty.deanLastName", is(group.getFaculty().getDeanLastName())))
                .andExpect(jsonPath("$._links.lessons.href", is("/lessons/groups/1/{day}")));
        verify(groupService).update(any());
    }

    @Test
    void getGroupFromRestTest() throws Exception {
        Group group = new Group(1, "Group", new Faculty(1, "Faculty", "Name", "Surname"));
        when(groupService.get(anyInt())).thenReturn(group);
        mvc.perform(get("/webapp/groups/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(group.getId())))
                .andExpect(jsonPath("$.name", is(group.getName())))
                .andExpect(jsonPath("$.faculty.name", is(group.getFaculty().getName())))
                .andExpect(jsonPath("$.faculty.deanFirstName", is(group.getFaculty().getDeanFirstName())))
                .andExpect(jsonPath("$.faculty.deanLastName", is(group.getFaculty().getDeanLastName())))
                .andExpect(jsonPath("$._links.lessons.href", is("/lessons/groups/1/{day}")));
        verify(groupService).get(1);
    }

    @Test
    void deleteGroupFromRestTest() throws Exception {
        mvc.perform(delete("/webapp/groups/1")).andExpect(status().is(204));
        verify(groupService).delete(1);
    }
}
