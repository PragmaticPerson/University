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
import ua.com.foxminded.service.models.faculty.Group;
import ua.com.foxminded.service.services.FacultyService;
import ua.com.foxminded.service.services.GroupService;

@Controller
@RequestMapping("/groups")
public class GroupController {
    private GroupService groupService;
    private FacultyService facultyService;

    private static final Logger LOGGER = LogManager.getLogger(GroupController.class);
    private static final String OPEN_VIEW = "Open view: {}";
    private static final String REDIRECT_TO = "Redirect to: {}";

    @Autowired
    public GroupController(GroupService groupService, FacultyService facultyService) {
        this.groupService = groupService;
        this.facultyService = facultyService;
    }

    @GetMapping("/add")
    public String addGroupGetRequest(Model model) throws ServiceException {
        LOGGER.debug("/groups/add GET request");
        model.addAttribute("group", new Group());
        model.addAttribute("faculties", facultyService.getAll());

        LOGGER.debug(OPEN_VIEW, "groups/addGroup");
        return "groups/addGroup";
    }

    @PostMapping("/add")
    public String addGroupPostRequest(@ModelAttribute @Valid Group group, BindingResult result, Model model)
            throws ServiceException {
        LOGGER.debug("/groups/add POST request to add group {}", group);
        if (result.hasErrors()) {
            String cause = result.getAllErrors().get(0).getDefaultMessage();
            LOGGER.info("Passed entity {} doesn't pass validation. Cause: {}", group, cause);

            model.addAttribute("error", cause);
            model.addAttribute("faculties", facultyService.getAll());

            return "groups/addGroup";
        }
        groupService.add(group);

        LOGGER.debug(REDIRECT_TO, "/groups/get-all");
        return "redirect:/groups/get-all";
    }

    @GetMapping("/get")
    public String getOneGroupGetRequest(@RequestParam int id, Model model) throws ServiceException {
        LOGGER.debug("/groups/get GET request by id {}", id);
        model.addAttribute("group", groupService.get(id));
        model.addAttribute("faculties", facultyService.getAll());

        LOGGER.debug(OPEN_VIEW, "groups/editGroup");
        return "groups/editGroup";
    }

    @PostMapping(value = "/get", params = "save")
    public String saveGroupPostRequestFromGetPage(@ModelAttribute @Valid Group group, BindingResult result, Model model)
            throws ServiceException {
        LOGGER.debug("/groups/get POST request to save group {}", group);
        if (result.hasErrors()) {
            String cause = result.getAllErrors().get(0).getDefaultMessage();
            LOGGER.info("Passed entity {} doesn't pass validation. Cause: {}", group, cause);

            model.addAttribute("error", cause);
            model.addAttribute("faculties", facultyService.getAll());
            return "groups/editGroup";
        }
        groupService.update(group);

        LOGGER.debug(REDIRECT_TO, "/groups/get-all");
        return "redirect:/groups/get-all";
    }

    @PostMapping(value = "/get", params = "delete")
    public String deleteGroupPostRequestFromGetPage(@ModelAttribute Group group, Model model) throws ServiceException {
        LOGGER.debug("/groups/get POST request to delete group {}", group);
        groupService.delete(group.getId());

        LOGGER.debug(REDIRECT_TO, "/groups/get-all");
        return "redirect:/groups/get-all";
    }

    @GetMapping("/get-all")
    public String getAllGroupGetRequest(Model model) throws ServiceException {
        LOGGER.debug("/groups/get-all get request");
        model.addAttribute("groups", groupService.getAll());

        LOGGER.debug(OPEN_VIEW, "groups/getAllGroups");
        return "groups/getAllGroups";
    }

    @PostMapping("/get-all")
    public String deleteGroupPostRequestFromGetAllPage(@RequestParam String id, Model model)
            throws NumberFormatException, ServiceException {
        LOGGER.debug("/groups/get-all POST request to delete with id {}", id);
        groupService.delete(Integer.parseInt(id));

        LOGGER.debug(REDIRECT_TO, "/groups/get-all");
        return "redirect:/groups/get-all";
    }
}
