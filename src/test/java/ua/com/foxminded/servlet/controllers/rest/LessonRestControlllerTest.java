package ua.com.foxminded.servlet.controllers.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
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

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(LessonRestController.class)
class LessonRestControlllerTest {
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
    private LessonRepository lessonRepository;
    @MockBean
    private TeacherRepository teacherRepository;

    @Autowired
    private MockMvc mvc;

    private static Lesson lesson1;
    private static Lesson lesson2;

    @BeforeAll
    static void setUpBeforeClass() {
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
    }

    @Test
    void getAllLessonsForGroupTest() throws Exception {
        when(lessonService.getLessonsByGroup(1)).thenReturn(Arrays.asList(lesson1));
        mvc.perform(get("/webapp/lessons/groups/1")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].group.id", is(1)))
                .andExpect(jsonPath("$[0].teacher.id", is(lesson1.getTeacher().getId())))
                .andExpect(jsonPath("$[0].subject.id", is(lesson1.getSubject().getId())))
                .andExpect(jsonPath("$[0].audience.id", is(lesson1.getAudience().getId())))
                .andExpect(jsonPath("$[0].day", is(lesson1.getDay().toString())))
                .andExpect(jsonPath("$[0].lessonNumber", is(lesson1.getLessonNumber().toString())));
        verify(lessonService).getLessonsByGroup(1);
    }

    @Test
    void getAllLessonsForGroupAndDayTest() throws Exception {
        when(lessonService.getLessonsByGroupAndDay(1, Weekdays.MONDAY)).thenReturn(Arrays.asList(lesson1));
        mvc.perform(get("/webapp/lessons/groups/1/monday")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].group.id", is(1)))
                .andExpect(jsonPath("$[0].teacher.id", is(lesson1.getTeacher().getId())))
                .andExpect(jsonPath("$[0].subject.id", is(lesson1.getSubject().getId())))
                .andExpect(jsonPath("$[0].audience.id", is(lesson1.getAudience().getId())))
                .andExpect(jsonPath("$[0].day", is("MONDAY")))
                .andExpect(jsonPath("$[0].lessonNumber", is(lesson1.getLessonNumber().toString())));
        verify(lessonService).getLessonsByGroupAndDay(1, Weekdays.MONDAY);
    }

    @Test
    void getAllLessonsForTeacherTest() throws Exception {
        when(lessonService.getLessonsByTeacher(1)).thenReturn(Arrays.asList(lesson1));
        mvc.perform(get("/webapp/lessons/teachers/1")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].group.id", is(lesson1.getGroup().getId())))
                .andExpect(jsonPath("$[0].teacher.id", is(1)))
                .andExpect(jsonPath("$[0].subject.id", is(lesson1.getSubject().getId())))
                .andExpect(jsonPath("$[0].audience.id", is(lesson1.getAudience().getId())))
                .andExpect(jsonPath("$[0].day", is(lesson1.getDay().toString())))
                .andExpect(jsonPath("$[0].lessonNumber", is(lesson1.getLessonNumber().toString())));
        verify(lessonService).getLessonsByTeacher(1);
    }

    @Test
    void getAllLessonsForTeacherAndDayTest() throws Exception {
        when(lessonService.getLessonsByTeacherAndDay(1, Weekdays.MONDAY)).thenReturn(Arrays.asList(lesson1));
        mvc.perform(get("/webapp/lessons/teachers/1/monday")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].group.id", is(lesson1.getGroup().getId())))
                .andExpect(jsonPath("$[0].teacher.id", is(1)))
                .andExpect(jsonPath("$[0].subject.id", is(lesson1.getSubject().getId())))
                .andExpect(jsonPath("$[0].audience.id", is(lesson1.getAudience().getId())))
                .andExpect(jsonPath("$[0].day", is("MONDAY")))
                .andExpect(jsonPath("$[0].lessonNumber", is(lesson1.getLessonNumber().toString())));
        verify(lessonService).getLessonsByTeacherAndDay(1, Weekdays.MONDAY);
    }

    @Test
    void postLessonToRestTest() throws Exception {
        setUpMocksBeforePostTests();

        MockHttpServletRequestBuilder request = post("/webapp/lessons");
        request.header("Content-Type", "application/json");
        request.content("{\"group\":{\"id\":\"1\"},\"teacher\":{\"id\":\"1\"},"
                + "\"subject\":{\"id\":\"1\"},\"day\":\"MONDAY\",\"lessonNumber\":\"FIFTH\","
                + "\"audience\":{\"id\":\"1\"},\"duration\":\"60\"}");

        mvc.perform(request).andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.group.id", is(lesson1.getGroup().getId())))
                .andExpect(jsonPath("$.teacher.id", is(1)))
                .andExpect(jsonPath("$.subject.id", is(lesson1.getSubject().getId())))
                .andExpect(jsonPath("$.audience.id", is(lesson1.getAudience().getId())))
                .andExpect(jsonPath("$.day", is("MONDAY")))
                .andExpect(jsonPath("$.lessonNumber", is(lesson1.getLessonNumber().toString())));
        verify(lessonService).add(any());
    }

    @Test
    void putLessonToRestTest() throws Exception {
        setUpMocksBeforePostTests();

        MockHttpServletRequestBuilder request = put("/webapp/lessons/1");
        request.header("Content-Type", "application/json");
        request.content("{\"group\":{\"id\":\"1\"},\"teacher\":{\"id\":\"1\"},"
                + "\"subject\":{\"id\":\"1\"},\"day\":\"MONDAY\",\"lessonNumber\":\"FIFTH\","
                + "\"audience\":{\"id\":\"1\"},\"duration\":\"60\"}");

        mvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.group.id", is(lesson1.getGroup().getId())))
                .andExpect(jsonPath("$.teacher.id", is(1)))
                .andExpect(jsonPath("$.subject.id", is(lesson1.getSubject().getId())))
                .andExpect(jsonPath("$.audience.id", is(lesson1.getAudience().getId())))
                .andExpect(jsonPath("$.day", is("MONDAY")))
                .andExpect(jsonPath("$.lessonNumber", is(lesson1.getLessonNumber().toString())));
        verify(lessonService).update(any());
    }

    @Test
    void deleteLessonFromRestTest() throws Exception {
        mvc.perform(delete("/webapp/lessons/1")).andExpect(status().is(204));
        verify(lessonService).delete(1);
    }

    private void setUpMocksBeforePostTests() {
        Teacher teacher1 = new Teacher(1, "Name", "Surname");
        teacher1.setSubjects(Arrays.asList(new Subject(1, "Subject1"), new Subject(5, "Subject5")));

        when(lessonRepository.getLessonsByGroupAndDay(anyInt(), any())).thenReturn(Arrays.asList(lesson1));
        when(lessonRepository.getLessonsByTeacherAndDay(anyInt(), any())).thenReturn(Arrays.asList(lesson1));
        when(lessonRepository.getLessonsByAudienceAndDay(anyInt(), any())).thenReturn(Arrays.asList(lesson1));
        when(teacherRepository.getOne(anyInt())).thenReturn(teacher1);
    }
}
