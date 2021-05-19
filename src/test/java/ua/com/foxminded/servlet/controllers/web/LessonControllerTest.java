package ua.com.foxminded.servlet.controllers.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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

import ua.com.foxminded.dao.repository.LessonRepository;
import ua.com.foxminded.dao.repository.TeacherRepository;
import ua.com.foxminded.service.models.audience.Audience;
import ua.com.foxminded.service.models.faculty.Group;
import ua.com.foxminded.service.models.people.Teacher;
import ua.com.foxminded.service.models.subject.Subject;
import ua.com.foxminded.service.models.timetable.Lesson;
import ua.com.foxminded.service.models.timetable.LessonNumber;
import ua.com.foxminded.service.models.timetable.Weekdays;
import ua.com.foxminded.service.services.AudienceService;
import ua.com.foxminded.service.services.GroupService;
import ua.com.foxminded.service.services.LessonService;
import ua.com.foxminded.service.services.SubjectService;
import ua.com.foxminded.service.services.TeacherService;
import ua.com.foxminded.servlet.controllers.TemplateResolverConfiguration;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(LessonController.class)
@Import(TemplateResolverConfiguration.class)
class LessonControllerTest {
    @MockBean
    private LessonService lessonService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private AudienceService audienceService;
    @MockBean
    private SubjectService subjectService;

    @MockBean
    private LessonRepository lessonRepos;
    @MockBean
    private TeacherRepository teacherRepos;

    @InjectMocks
    private LessonController lessonsController;

    @Autowired
    private MockMvc mockMvc;

    private Lesson lesson1;
    private Lesson lesson2;
    private Weekdays[] days;

    @BeforeEach
    void setUpBeforeClass() {
        lesson1 = new Lesson();
        lesson2 = new Lesson();
        Teacher teacher1 = new Teacher(1, "Name", "Surname");
        teacher1.setSubjects(Arrays.asList(new Subject(1, "Subject1"), new Subject(5, "Subject5")));

        lesson1.setAudience(new Audience(1, 101, 50));
        lesson1.setDay(Weekdays.MONDAY);
        lesson1.setDuration(90);
        lesson1.setGroup(new Group(1, "Group1"));
        lesson1.setLessonNumber(LessonNumber.FIFTH);
        lesson1.setSubject(new Subject(1, "Subject1"));
        lesson1.setTeacher(teacher1);

        lesson2.setAudience(new Audience(5, 105, 120));
        lesson2.setDay(Weekdays.WEDNESDAY);
        lesson2.setDuration(120);
        lesson2.setGroup(new Group(4, "Group4"));
        lesson2.setLessonNumber(LessonNumber.FIRST);
        lesson2.setSubject(new Subject(10, "Subject10"));
        lesson2.setTeacher(new Teacher(3, "Name", "Surname"));

        days = Weekdays.values();
    }

    @Test
    void getAllForGroupGetRequest() throws Exception {
        List<Lesson> expectedLessons = Arrays.asList(lesson1, lesson2);
        when((lessonService.getLessonsByGroup(anyInt()))).thenReturn(expectedLessons);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lessons/by-group?id=1");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("lessons/getAllLessonsForGroup"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("source"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lessons"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("days"))
                .andExpect(MockMvcResultMatchers.model().attribute("lessons", expectedLessons))
                .andExpect(MockMvcResultMatchers.model().attribute("days", days));
    }

    @Test
    void getAllForTeacher() throws Exception {
        List<Lesson> expectedLessons = Arrays.asList(lesson1, lesson2);
        when((lessonService.getLessonsByTeacher(anyInt()))).thenReturn(expectedLessons);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lessons/by-teacher?id=1");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("lessons/getAllLessonsForTeacher"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("source"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lessons"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("days"))
                .andExpect(MockMvcResultMatchers.model().attribute("lessons", expectedLessons))
                .andExpect(MockMvcResultMatchers.model().attribute("days", days));
    }

    @Test
    void addGetRequest() throws Exception {
        when(lessonService.getLesson(anyLong())).thenReturn(lesson1);
        when(groupService.getAll()).thenReturn(Arrays.asList(new Group(1, "Name")));
        when(audienceService.getAll()).thenReturn(Arrays.asList(new Audience(1, 102, 40)));
        when(teacherService.getAll()).thenReturn(Arrays.asList(new Teacher(1, "Name", "Surname")));
        when(subjectService.getAll()).thenReturn(Arrays.asList(new Subject(1, "Subject name")));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lessons/add");
        request.param("id", "1");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("lessons/addLesson"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lesson"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("groups"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("days"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("numbers"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("teachers"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("audiences"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("subjects"));

        verify(groupService).getAll();
        verify(audienceService).getAll();
        verify(teacherService).getAll();
        verify(subjectService).getAll();
    }

    @Test
    void addPostRequestToSaveDataWhenValidationSuccessfulTest() throws Exception {
        lesson1.setDay(Weekdays.THURSDAY);
        setUpMocksBeforePostTests();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lessons/add");
        request.param("save", "");
        request.flashAttr("lesson", lesson1);
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/lessons/by-group?id=1"));
        verify(lessonService).add(any());
    }

    @Test
    void addPostRequestToSaveDataWhenValidationFailedTest() throws Exception {
        setUpMocksBeforePostTests();

        lesson1.setSubject(new Subject(3, ""));
        String errorMessage = "Teacher doesn't teach this subject. Subjects: Subject1, Subject5.";

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lessons/add");
        request.param("save", "");
        request.flashAttr("lesson", lesson1);
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("lessons/addLesson"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", errorMessage));
    }
    
    @Test
    void getCurrentGetRequest() throws Exception {
        when(lessonService.getLesson(anyLong())).thenReturn(lesson1);
        when(audienceService.getAll()).thenReturn(Arrays.asList(new Audience(1, 102, 40)));
        when(teacherService.getAll()).thenReturn(Arrays.asList(new Teacher(1, "Name", "Surname")));
        when(subjectService.getAll()).thenReturn(Arrays.asList(new Subject(1, "Subject name")));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lessons/get");
        request.param("id", "1");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("lessons/editLesson"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lesson"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("teachers"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("audiences"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("subjects"))
                .andExpect(MockMvcResultMatchers.model().attribute("lesson", lesson1));

        verify(lessonService).getLesson(anyLong());
        verify(audienceService).getAll();
        verify(teacherService).getAll();
        verify(subjectService).getAll();
    }

    @Test
    void getCurrentPostRequestToSaveDataWhenValidationSuccessfulTest() throws Exception {
        lesson1.setDay(Weekdays.THURSDAY);
        setUpMocksBeforePostTests();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lessons/get");
        request.param("save", "");
        request.flashAttr("lesson", lesson1);
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/lessons/by-group?id=1"));
        verify(lessonService).update(any());
    }

    @Test
    void getCurrentPostRequestToSaveDataWhenValidationFailedTest() throws Exception {
        setUpMocksBeforePostTests();

        lesson1.setSubject(new Subject(3, ""));
        String errorMessage = "Teacher doesn't teach this subject. Subjects: Subject1, Subject5.";

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lessons/get");
        request.param("save", "");
        request.flashAttr("lesson", lesson1);
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("lessons/editLesson"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", errorMessage));
    }

    @Test
    void getCurrentPostRequestToDeleteData() throws Exception {
        setUpMocksBeforePostTests();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lessons/get");
        request.param("delete", "");
        request.flashAttr("lesson", lesson1);
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/lessons/by-group?id=1"));
        verify(lessonService).delete(anyLong());
    }

    @Test
    void deleteLessonPostRequest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lessons/delete");
        request.header("referer", "/some/url");
        request.param("id", "1");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.view().name("redirect:/some/url"));
        verify(lessonService).delete(anyLong());
    }

    private void setUpMocksBeforePostTests() {
        Teacher teacher1 = new Teacher(1, "Name", "Surname");
        teacher1.setSubjects(Arrays.asList(new Subject(1, "Subject1"), new Subject(5, "Subject5")));

        when(lessonRepos.getLessonsByGroupAndDay(anyInt(), any())).thenReturn(Arrays.asList(lesson1));
        when(lessonRepos.getLessonsByTeacherAndDay(anyInt(), any())).thenReturn(Arrays.asList(lesson1));
        when(lessonRepos.getLessonsByAudienceAndDay(anyInt(), any())).thenReturn(Arrays.asList(lesson1));
        when(teacherRepos.getOne(anyInt())).thenReturn(teacher1);
    }
}
