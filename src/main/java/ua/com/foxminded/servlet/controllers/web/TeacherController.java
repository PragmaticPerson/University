package ua.com.foxminded.servlet.controllers.web;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.people.Teacher;
import ua.com.foxminded.service.services.SubjectService;
import ua.com.foxminded.service.services.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    private TeacherService teacherService;
    private SubjectService subjectService;

    private static final Logger LOGGER = LogManager.getLogger(TeacherController.class);
    private static final String OPEN_VIEW = "Open view: {}";
    private static final String REDIRECT_TO = "Redirect to: {}";

    @Autowired
    public TeacherController(TeacherService teacherService, SubjectService subjectService) {
        this.teacherService = teacherService;
        this.subjectService = subjectService;
    }

    @GetMapping("/add")
    public String addTeacherGetRequest(Model model) throws ServiceException {
        LOGGER.debug("/teachers/add GET request");
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("allSubjects", subjectService.getAll());

        LOGGER.debug(OPEN_VIEW, "teachers/addTeacher");
        return "teachers/addTeacher";
    }

    @PostMapping("/add")
    public String addTeacherPostRequest(@ModelAttribute @Valid Teacher teacher, BindingResult result, Model model)
            throws ServiceException {
        LOGGER.debug("/teachers/add POST request to add teacher {}", teacher);
        if (result.hasErrors()) {
            String cause = result.getAllErrors().get(0).getDefaultMessage();
            LOGGER.info("Passed entity {} doesn't pass validation. Cause: {}", teacher, cause);

            model.addAttribute("error", cause);
            model.addAttribute("allSubjects", subjectService.getAll());
            return "teachers/addTeacher";
        }
        teacherService.add(teacher);

        LOGGER.debug(REDIRECT_TO, "/teachers/get-all");
        return "redirect:/teachers/get-all";
    }

    @GetMapping("/get")
    public String getTeacherGetRequest(@RequestParam int id, Model model) throws ServiceException {
        LOGGER.debug("/teachers/get GET request by id {}", id);
        model.addAttribute("teacher", teacherService.get(id));
        model.addAttribute("allSubjects", subjectService.getAll());

        LOGGER.debug(OPEN_VIEW, "teachers/editTeacher");
        return "teachers/editTeacher";
    }

    @PostMapping(value = "/get", params = "save")
    public String saveTeacherPostRequestFromGetPage(@ModelAttribute @Valid Teacher teacher, BindingResult result,
            Model model) throws ServiceException {
        LOGGER.info("/teachers/get POST request to save teacher {}", teacher);
        if (result.hasErrors()) {
            String cause = result.getAllErrors().get(0).getDefaultMessage();
            LOGGER.info("Passed entity {} doesn't pass validation. Cause: {}", teacher, cause);

            model.addAttribute("error", cause);
            model.addAttribute("allSubjects", subjectService.getAll());
            return "teachers/editTeacher";
        }
        teacherService.update(teacher);

        LOGGER.debug(REDIRECT_TO, "/teachers/get-all");
        return "redirect:/teachers/get-all";
    }

    @PostMapping(value = "/get", params = "delete")
    public String deleteTeacherPostRequestFromGetPage(@ModelAttribute Teacher teacher, Model model)
            throws ServiceException {
        LOGGER.info("/teachers/get POST request to delete teacher {}", teacher);
        teacherService.delete(teacher.getId());

        LOGGER.debug(REDIRECT_TO, "/teachers/get-all");
        return "redirect:/teachers/get-all";
    }

    @GetMapping("/get-all")
    public String getAllTeachersGetRequest(Model model) throws ServiceException {
        LOGGER.debug("/teachers/get-all get request");
        model.addAttribute("teachers", teacherService.getAll());

        LOGGER.debug(OPEN_VIEW, "teachers/getAllTeachers");
        return "teachers/getAllTeachers";
    }

    @PostMapping("/get-all")
    public String deleteSubjectFromTeacherPostRequestFromGetAllPage(@RequestParam String teacherId,
            @RequestParam String subjectId, Model model) throws NumberFormatException, ServiceException {
        LOGGER.debug("/teachers/get-all POST request to delete subject at teacher with teacherId {} and subjectId {}",
                teacherId, subjectId);
        teacherService.deleteSubject(Integer.parseInt(teacherId), Integer.parseInt(subjectId));

        LOGGER.debug(REDIRECT_TO, "/teachers/get-all");
        return "redirect:/teachers/get-all";
    }
}