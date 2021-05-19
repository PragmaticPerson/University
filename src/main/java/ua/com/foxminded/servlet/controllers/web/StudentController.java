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
import ua.com.foxminded.service.models.people.Student;
import ua.com.foxminded.service.services.GroupService;
import ua.com.foxminded.service.services.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {
    private StudentService studentService;
    private GroupService groupService;

    private static final Logger LOGGER = LogManager.getLogger(StudentController.class);
    private static final String OPEN_VIEW = "Open view: {}";
    private static final String REDIRECT_TO = "Redirect to: {}";

    @Autowired
    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping("/add")
    public String addStudentGetRequest(Model model) throws ServiceException {
        LOGGER.debug("/students/add GET request");
        model.addAttribute("student", new Student());
        model.addAttribute("groups", groupService.getAll());

        LOGGER.debug(OPEN_VIEW, "students/addStudent");
        return "students/addStudent";
    }

    @PostMapping("/add")
    public String addStudentPostRequest(@ModelAttribute @Valid Student student, BindingResult result, Model model)
            throws ServiceException {
        LOGGER.debug("/students/add POST request to add student {}", student);
        if (result.hasErrors()) {
            String cause = result.getAllErrors().get(0).getDefaultMessage();
            LOGGER.info("Passed entity {} doesn't pass validation. Cause: {}", student, cause);

            model.addAttribute("error", cause);
            return "students/addStudent";
        }
        studentService.add(student);
        LOGGER.debug(REDIRECT_TO, "/students/get-all");
        return "redirect:/students/get-all";
    }

    @GetMapping("/get")
    public String getOneStudentGetRequest(@RequestParam int id, Model model) throws ServiceException {
        LOGGER.debug("/students/get GET request by id {}", id);
        model.addAttribute("student", studentService.get(id));
        model.addAttribute("groups", groupService.getAll());

        LOGGER.debug(OPEN_VIEW, "students/editStudent");
        return "students/editStudent";
    }

    @PostMapping(value = "/get", params = "save")
    public String saveStudentPostRequestFromGetPage(@ModelAttribute @Valid Student student, BindingResult result,
            Model model) throws ServiceException {
        LOGGER.debug("/students/get POST request to save student {}", student);
        if (result.hasErrors()) {
            String cause = result.getAllErrors().get(0).getDefaultMessage();
            LOGGER.info("Passed entity {} doesn't pass validation. Cause: {}", student, cause);

            model.addAttribute("error", cause);
            return "students/editStudent";
        }
        studentService.update(student);

        LOGGER.debug(REDIRECT_TO, "/students/get-all");
        return "redirect:/students/get-all";
    }

    @PostMapping(value = "/get", params = "delete")
    public String deleteStudentPostRequestFromGetPage(@ModelAttribute Student student, Model model)
            throws ServiceException {
        LOGGER.debug("/students/get POST request to delete student {}", student);
        studentService.delete(student.getId());

        LOGGER.debug(REDIRECT_TO, "/students/get-all");
        return "redirect:/students/get-all";
    }

    @GetMapping("/get-all")
    public String getAllStudentsGetRequest(Model model) throws ServiceException {
        LOGGER.debug("/students/get-all get request");
        model.addAttribute("students", studentService.getAll());

        LOGGER.debug(OPEN_VIEW, "students/getAllStudents");
        return "students/getAllStudents";
    }

    @PostMapping("/get-all")
    public String deleteStudentPostRequestFromGetAllPage(@RequestParam String id, Model model)
            throws NumberFormatException, ServiceException {
        LOGGER.debug("/students/get-all POST request to delete with id {}", id);
        studentService.delete(Integer.parseInt(id));

        LOGGER.debug(REDIRECT_TO, "/students/get-all");
        return "redirect:/students/get-all";
    }
}
