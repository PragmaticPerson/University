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
import ua.com.foxminded.service.models.faculty.Faculty;
import ua.com.foxminded.service.services.FacultyService;

@Controller
@RequestMapping("/faculties")
public class FacultyController {
    private FacultyService facultyService;

    private static final Logger LOGGER = LogManager.getLogger(FacultyController.class);
    private static final String OPEN_VIEW = "Open view: {}";
    private static final String REDIRECT_TO = "Redirect to: {}";

    @Autowired
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/add")
    public String addFacultyGetRequest(Model model) {
        LOGGER.debug("/faculties/add GET request");
        model.addAttribute("faculty", new Faculty());

        LOGGER.debug(OPEN_VIEW, "faculties/addFaculty");
        return "faculties/addFaculty";
    }

    @PostMapping("/add")
    public String addFacultyPostRequest(@ModelAttribute @Valid Faculty faculty, BindingResult result, Model model)
            throws ServiceException {
        LOGGER.debug("/faculties/add POST request to add faculty {}", faculty);
        if (result.hasErrors()) {
            String cause = result.getAllErrors().get(0).getDefaultMessage();
            LOGGER.info("Passed entity {} doesn't pass validation. Cause: {}", faculty, cause);
            model.addAttribute("error", cause);

            return "faculties/addFaculty";
        }
        facultyService.add(faculty);

        LOGGER.debug(REDIRECT_TO, "/faculties/get-all");
        return "redirect:/faculties/get-all";
    }

    @GetMapping("/get")
    public String getOneFacultyGetRequest(@RequestParam int id, Model model) throws ServiceException {
        LOGGER.debug("/faculties/get GET request by id {}", id);
        model.addAttribute("faculty", facultyService.get(id));

        LOGGER.debug(OPEN_VIEW, "faculties/editFaculty");
        return "faculties/editFaculty";
    }

    @PostMapping(value = "/get", params = "save")
    public String saveFacultyPostRequestFromGetPage(@ModelAttribute @Valid Faculty faculty, BindingResult result,
            Model model) throws ServiceException {
        LOGGER.debug("/faculties/get POST request to save faculty {}", faculty);
        if (result.hasErrors()) {
            String cause = result.getAllErrors().get(0).getDefaultMessage();
            LOGGER.info("Passed entity {} doesn't pass validation. Cause: {}", faculty, cause);
            model.addAttribute("error", cause);

            return "faculties/editFaculty";
        }
        facultyService.update(faculty);

        LOGGER.debug(REDIRECT_TO, "/faculties/get-all");
        return "redirect:/faculties/get-all";
    }

    @PostMapping(value = "/get", params = "delete")
    public String deleteFacultyPostRequestFromGetPage(@ModelAttribute Faculty faculty, Model model)
            throws ServiceException {
        LOGGER.debug("/faculties/get POST request to delete faculty {}", faculty);
        facultyService.delete(faculty.getId());

        LOGGER.debug(REDIRECT_TO, "/faculties/get-all");
        return "redirect:/faculties/get-all";
    }

    @GetMapping("/get-all")
    public String getAllFacultiesGetRequest(Model model) throws ServiceException {
        LOGGER.debug("/faculties/get-all get request");
        model.addAttribute("faculties", facultyService.getAll());

        LOGGER.debug(OPEN_VIEW, "faculties/getAllFaculties");
        return "faculties/getAllFaculties";
    }

    @PostMapping("/get-all")
    public String deleteFacultyPostRequestFromGetAllPage(@RequestParam String id, Model model)
            throws NumberFormatException, ServiceException {
        LOGGER.debug("/faculties/get-all POST request to delete with id {}", id);
        facultyService.delete(Integer.parseInt(id));

        LOGGER.debug(REDIRECT_TO, "/faculties/get-all");
        return "redirect:/faculties/get-all";
    }
}
