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
import ua.com.foxminded.service.models.audience.Audience;
import ua.com.foxminded.service.services.AudienceService;

@Controller
@RequestMapping("/audiences")
public class AudienceController {
    private AudienceService audienceService;

    private static final Logger LOGGER = LogManager.getLogger(AudienceController.class);
    private static final String OPEN_VIEW = "Open view: {}";
    private static final String REDIRECT_TO = "Redirect to: {}";

    @Autowired
    public AudienceController(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    @GetMapping("/add")
    public String addAudienceGetRequest(Model model) {
        LOGGER.debug("/audiences/add GET request");
        model.addAttribute("audience", new Audience());

        LOGGER.debug(OPEN_VIEW, "audiences/addAudience");
        return "audiences/addAudience";
    }

    @PostMapping("/add")
    public String addAudiencePostRequest(@ModelAttribute @Valid Audience audience, BindingResult result, Model model)
            throws ServiceException {
        LOGGER.debug("/audiences/add POST request to add audience {}", audience);
        if (result.hasErrors()) {
            String cause = result.getAllErrors().get(0).getDefaultMessage();
            LOGGER.info("Passed entity {} doesn't pass validation. Cause: {}", audience, cause);

            model.addAttribute("error", cause);
            return "audiences/addAudience";
        }
        audienceService.add(audience);

        LOGGER.debug(REDIRECT_TO, "/audiences/get-all");
        return "redirect:/audiences/get-all";

    }

    @GetMapping("/get")
    public String getOneAudienceGetRequest(@RequestParam("id") int audienceId, Model model) throws ServiceException {
        LOGGER.debug("/audiences/get GET request by id {}", audienceId);
        model.addAttribute("audience", audienceService.get(audienceId));
        LOGGER.debug(OPEN_VIEW, "audiences/editAudience");
        return "audiences/editAudience";
    }

    @PostMapping(value = "/get", params = "save")
    public String saveOneAudiencePostRequestFromGetPage(@ModelAttribute @Valid Audience audience, BindingResult result,
            Model model) throws ServiceException {
        LOGGER.debug("/audiences/get POST request to save audience {}", audience);
        if (result.hasErrors()) {
            String cause = result.getAllErrors().get(0).getDefaultMessage();
            LOGGER.info("Passed entity {} doesn't pass validation. Cause: {}", audience, cause);

            model.addAttribute("error", cause);
            return "audiences/editAudience";
        }

        audienceService.update(audience);

        LOGGER.debug(REDIRECT_TO, "/audiences/get-all");
        return "redirect:/audiences/get-all";
    }

    @PostMapping(value = "/get", params = "delete")
    public String deleteAudiencePostRequestFromGetPage(@ModelAttribute Audience audience, Model model)
            throws ServiceException {
        LOGGER.debug("/audiences/get POST request to delete audience {}", audience);

        audienceService.delete(audience.getId());

        LOGGER.debug(REDIRECT_TO, "/audiences/get-all");
        return "redirect:/audiences/get-all";
    }

    @GetMapping("/get-all")
    public String getAllAudiencesGetRequest(Model model) throws ServiceException {
        LOGGER.debug("/audiences/get-all get request");
        model.addAttribute("audiences", audienceService.getAll());

        LOGGER.debug(OPEN_VIEW, "audiences/getAllAudiences");
        return "audiences/getAllAudiences";
    }

    @PostMapping("/get-all")
    public String deleteAudiencePostRequestFromGetAllPage(@RequestParam String id, Model model)
            throws NumberFormatException, ServiceException {
        LOGGER.debug("/audiences/get-all POST request to delete with id {}", id);
        audienceService.delete(Integer.parseInt(id));

        LOGGER.debug(REDIRECT_TO, "/audiences/get-all");
        return "redirect:/audiences/get-all";
    }
}
