package ua.com.foxminded.servlet.controllers.web;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.timetable.Lesson;
import ua.com.foxminded.service.models.timetable.LessonNumber;
import ua.com.foxminded.service.models.timetable.Weekdays;
import ua.com.foxminded.service.services.AudienceService;
import ua.com.foxminded.service.services.GroupService;
import ua.com.foxminded.service.services.LessonService;
import ua.com.foxminded.service.services.SubjectService;
import ua.com.foxminded.service.services.TeacherService;

@Controller
@RequestMapping("/lessons")
public class LessonController {
    private LessonService lessonService;
    private GroupService groupService;
    private TeacherService teacherService;
    private AudienceService audienceService;
    private SubjectService subjectService;

    private static final Logger LOGGER = LogManager.getLogger(LessonController.class);
    private static final String OPEN_VIEW = "Open view: {}";

    @Autowired
    public LessonController(LessonService lessonService, GroupService groupService, TeacherService teacherService,
            AudienceService audienceService, SubjectService subjectService) {
        this.lessonService = lessonService;
        this.groupService = groupService;
        this.teacherService = teacherService;
        this.audienceService = audienceService;
        this.subjectService = subjectService;
    }

    @GetMapping("/add")
    public String addLessonGetRequest(Model model) throws ServiceException {
        LOGGER.debug("/lessons/add GET request");
        model.addAttribute("lesson", new Lesson());
        setUpSubclasses(model);

        LOGGER.debug("/lessons/get GET. Add attributes groups, teachers, days, numbers, audiences, subjects");

        LOGGER.debug(OPEN_VIEW, "lessons/addLesson");
        return "lessons/addLesson";
    }

    @PostMapping("/add")
    public String addLessonPostRequest(@ModelAttribute @Valid Lesson lesson, BindingResult result, Model model)
            throws ServiceException {
        LOGGER.debug("/lessons/add POST request to add lesson {}", lesson);
        if (result.hasErrors()) {
            LOGGER.info("Passed entity {} doesn't pass validation", lesson);
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());

            setUpSubclasses(model);

            model.addAttribute("lesson", lesson);
            return "lessons/addLesson";
        }

        lessonService.add(lesson);

        LOGGER.debug("Redirect to: /lessons/by-group?id={}", lesson.getGroup().getId());
        return "redirect:/lessons/by-group?id=" + lesson.getGroup().getId();
    }

    @GetMapping("/by-group")
    public String getAllLessonsForGroupGetRequest(@RequestParam("id") int groupId, Model model)
            throws ServiceException {
        LOGGER.debug("/lessons/by-group GET request with group id {}", groupId);
        List<Lesson> lessons = lessonService.getLessonsByGroup(groupId);
        model.addAttribute("lessons", lessons);
        model.addAttribute("source", "group "
                + (lessons.isEmpty() ? groupService.get(groupId).getName() : lessons.get(0).getGroup().getName()));
        setUpDayAndLesson(model);

        LOGGER.debug(OPEN_VIEW, "lessons/getAllLessonsForGroup");
        return "lessons/getAllLessonsForGroup";
    }

    @GetMapping("/by-teacher")
    public String getAllLessonsForTeacherGetRequest(@RequestParam("id") int teacherId, Model model)
            throws ServiceException {
        LOGGER.debug("/lessons/by-teacher GET request with teacher id {}", teacherId);
        List<Lesson> lessons = lessonService.getLessonsByTeacher(teacherId);
        model.addAttribute("lessons", lessons);
        model.addAttribute("source",
                "teacher " + (lessons.isEmpty()
                        ? teacherService.get(teacherId).getName() + " " + teacherService.get(teacherId).getSurname()
                        : lessons.get(0).getTeacher().getName() + " " + lessons.get(0).getTeacher().getSurname()));
        setUpDayAndLesson(model);

        LOGGER.debug(OPEN_VIEW, "lessons/getAllLessonsForTeacher");
        return "lessons/getAllLessonsForTeacher";
    }

    @GetMapping("/get")
    public String getOneLessonGetRequest(@RequestParam long id, Model model) throws ServiceException {
        LOGGER.debug("/lessons/get GET request by id {}", id);

        model.addAttribute("lesson", lessonService.getLesson(id));
        setUpSubclasses(model);

        LOGGER.debug(OPEN_VIEW, "lessons/editLesson");
        return "lessons/editLesson";
    }

    @PostMapping(value = "/get", params = "save")
    public String saveLessonPostRequestFromGetPage(@ModelAttribute @Valid Lesson lesson, BindingResult result,
            Model model, @RequestParam String save) throws ServiceException {
        LOGGER.debug("/lessons/get POST request to save lesson {}", lesson);
        if (result.hasErrors()) {
            String cause = result.getAllErrors().get(0).getDefaultMessage();
            LOGGER.info("Passed entity {} doesn't pass validation. Cause: {}", lesson, cause);
            model.addAttribute("error", cause);

            setUpSubclasses(model);

            model.addAttribute("lesson", lesson);
            return "lessons/editLesson";
        }
        lessonService.update(lesson);

        LOGGER.debug(OPEN_VIEW, "/lessons/by-group?id=" + lesson.getGroup().getId());
        return "redirect:/lessons/by-group?id=" + lesson.getGroup().getId();

    }

    @PostMapping(value = "/get", params = "delete")
    public String deleteLessonPostRequestFromGetPage(@ModelAttribute Lesson lesson, Model model,
            @RequestParam String delete) throws ServiceException {
        LOGGER.debug("/lessons/get POST request to delete lesson {}", lesson);
        lessonService.delete(lesson.getId());

        LOGGER.debug(OPEN_VIEW, "/lessons/by-group?id=" + lesson.getGroup().getId());
        return "redirect:/lessons/by-group?id=" + lesson.getGroup().getId();

    }

    @PostMapping("/delete")
    public String deleteLessonPostRequestFromGetAllPage(@RequestParam long id, @RequestHeader String referer,
            Model model) throws ServiceException {
        LOGGER.debug("/lessons/delete POST request to delete lesson by id {}", id);
        lessonService.delete(id);

        LOGGER.debug("Redirect to: {}", referer);
        return "redirect:" + referer;
    }

    private void setUpDayAndLesson(Model model) {
        model.addAttribute("currLesson", new Lesson());
        model.addAttribute("days", Weekdays.values());
    }

    private void setUpSubclasses(Model model) throws ServiceException {
        model.addAttribute("groups", groupService.getAll());
        model.addAttribute("teachers", teacherService.getAll());
        model.addAttribute("days", Weekdays.values());
        model.addAttribute("numbers", LessonNumber.values());
        model.addAttribute("audiences", audienceService.getAll());
        model.addAttribute("subjects", subjectService.getAll());
    }
}
