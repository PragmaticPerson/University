package ua.com.foxminded.servlet.controllers.web;

import static org.mockito.Mockito.*;

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

import ua.com.foxminded.dao.repository.AudienceRepository;
import ua.com.foxminded.service.models.audience.Audience;
import ua.com.foxminded.service.services.AudienceService;
import ua.com.foxminded.servlet.controllers.TemplateResolverConfiguration;
import ua.com.foxminded.validator.AudienceValidator;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(AudienceController.class)
@Import(TemplateResolverConfiguration.class)
class AudienceControllerTest {
    @MockBean
    private AudienceService audienceService;
    @MockBean
    private AudienceRepository audienceRepos;

    @InjectMocks
    private AudienceValidator audienceValidator;
    @InjectMocks
    private AudienceController audienceController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addEntityGetRequestTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/audiences/add");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("audiences/addAudience"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("audience"));
    }

    @Test
    void addEntityWithSuccessfulValidationPostRequestTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/audiences/add");
        request.flashAttr("audience", new Audience(215, 58));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.redirectedUrl("/audiences/get-all"));
        verify(audienceService).add(any());
    }

    @Test
    void addEntityWithFailedValidationPostRequestTest() throws Exception {
        when(audienceRepos.countByNumberAndIdNot(anyInt(), anyInt())).thenReturn(3);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/audiences/add");
        request.flashAttr("audience", new Audience(215, -125));
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.view().name("audiences/addAudience"));
    }

    @Test
    void getCurrentGetRequestTest() throws Exception {
        Audience expectedAudience = new Audience(1, 101, 40);
        when(audienceService.get(anyInt())).thenReturn(expectedAudience);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/audiences/get?id=1");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("audiences/editAudience"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("audience"))
                .andExpect(MockMvcResultMatchers.model().attribute("audience", expectedAudience));
    }

    @Test
    void getCurrentPostRequestToUpdateDataWithSuccessfulValidationTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/audiences/get");
        request.flashAttr("audience", new Audience(215, 58));
        request.param("save", "save");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.redirectedUrl("/audiences/get-all"));
        verify(audienceService).update(any());
    }

    @Test
    void getCurrentPostRequestToUpdateDataWithFailedValidationTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/audiences/get");
        request.flashAttr("audience", new Audience(215, -58));
        request.param("save", "save");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("audiences/editAudience"));
    }

    @Test
    void getCurrentPostRequestToDeleteDataTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/audiences/get");
        request.flashAttr("audience", new Audience(1, 101, 160));
        request.param("delete", "delete");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/audiences/get-all"));
        verify(audienceService).delete(anyInt());
    }

    @Test
    void getAllGetRequestTest() throws Exception {
        List<Audience> expectedAudiences = Arrays.asList(new Audience(1, 101, 40), new Audience(2, 102, 60));
        when(audienceService.getAll()).thenReturn(expectedAudiences);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/audiences/get-all");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("audiences/getAllAudiences"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("audiences"))
                .andExpect(MockMvcResultMatchers.model().attribute("audiences", expectedAudiences));
    }

    @Test
    void getAllPostRequestTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/audiences/get-all");
        request.param("id", "7");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/audiences/get-all"));
        verify(audienceService).delete(anyInt());
    }
}
