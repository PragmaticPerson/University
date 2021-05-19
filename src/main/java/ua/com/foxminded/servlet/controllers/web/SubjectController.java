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
import ua.com.foxminded.service.models.subject.Subject;
import ua.com.foxminded.service.services.SubjectService;

@Controller
@RequestMapping("/subjects")
public class SubjectController {
    private SubjectService subjectService;

    private static final Logger LOGGER = LogManager.getLogger(SubjectController.class);
    private static final String OPEN_VIEW = "Open view: {}";
    private static final String REDIRECT_TO = "Redirect to: {}";

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/add")
    public String addSubjectGetRequest(Model model) {
        LOGGER.debug("/subjects/add GET request");
        model.addAttribute("subject", new Subject());

        LOGGER.debug(OPEN_VIEW, "subjects/addSubject");
        return "subjects/addSubject";
    }

    @PostMapping("/add")
    public String addSubjectPostRequest(@ModelAttribute @Valid Subject subject, BindingResult result, Model model)
            throws ServiceException {
        if (result.hasErrors()) {
            String cause = result.getAllErrors().get(0).getDefaultMessage();
            LOGGER.info("Passed entity {} doesn't pass validation. Cause: {}", subject, cause);

            model.addAttribute("error", cause);
            return "subjects/addSubject";
        }
        LOGGER.debug("/subjects/add POST request with model attribute {}", subject);
        subjectService.add(subject);

        LOGGER.debug(REDIRECT_TO, "/subjects/get-all");
        return "redirect:/subjects/get-all";
    }

    @GetMapping("/get")
    public String getSubjectGetRequest(@RequestParam int id, Model model) throws ServiceException {
        LOGGER.debug("/subjects/get GET request by id {}", id);
        model.addAttribute("subject", subjectService.get(id));

        LOGGER.debug(OPEN_VIEW, "subjects/editSubject");
        return "subjects/editSubject";
    }

    @PostMapping(value = "/get", params = "save")
    public String saveSubjectPostRequestFromGetPage(@ModelAttribute @Valid Subject subject, BindingResult result,
            Model model) throws ServiceException {
        LOGGER.debug("/subjects/get POST request to save subject {}", subject);
        if (result.hasErrors()) {
            String cause = result.getAllErrors().get(0).getDefaultMessage();
            LOGGER.info("Passed entity {} doesn't pass validation. Cause: {}", subject, cause);

            model.addAttribute("error", cause);
            return "subjects/editSubject";
        }
        subjectService.update(subject);

        LOGGER.debug(REDIRECT_TO, "/subjects/get-all");
        return "redirect:/subjects/get-all";
    }

    @PostMapping(value = "/get", params = "delete")
    public String deleteSubjectPostRequestFromGetPage(@ModelAttribute Subject subject, Model model)
            throws ServiceException {
        LOGGER.debug("/subjects/get POST request to delete subject {}", subject);
        subjectService.delete(subject.getId());

        LOGGER.debug(REDIRECT_TO, "/subjects/get-all");
        return "redirect:/subjects/get-all";
    }

    @GetMapping("/get-all")
    public String getAllSubjectsGetRequest(Model model) throws ServiceException {
        LOGGER.debug("/subjects/get-all GET request");
        model.addAttribute("subjects", subjectService.getAll());

        LOGGER.debug(OPEN_VIEW, "subjects/getAllSubjects");
        return "subjects/getAllSubjects";
    }

    @PostMapping("/get-all")
    public String deleteSubjectPostRequestFromGetAllPage(@RequestParam String id, Model model)
            throws NumberFormatException, ServiceException {
        LOGGER.debug("/subjects/get-all POST request to delete with id {}", id);
        subjectService.delete(Integer.parseInt(id));

        LOGGER.debug(REDIRECT_TO, "/subjects/get-all");
        return "redirect:/subjects/get-all";
    }
}
