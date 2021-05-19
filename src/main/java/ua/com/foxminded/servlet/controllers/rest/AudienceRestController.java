package ua.com.foxminded.servlet.controllers.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.*;
import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.audience.Audience;
import ua.com.foxminded.service.services.AudienceService;

@RestController
@RequestMapping("/webapp/audiences")
public class AudienceRestController {
    private AudienceService audienceService;

    @Autowired
    public AudienceRestController(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    @Operation(summary = "Gets all avaliable audiences")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the audiences", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Audience.class))) }),
            @ApiResponse(responseCode = "404", description = "No audiences found", content = @Content) })
    @GetMapping
    public List<Audience> all() throws ServiceException {
        return audienceService.getAll();
    }

    @Operation(summary = "Create an audience")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Audience created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Audience.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Audience add(@RequestBody @Valid Audience audience) throws ServiceException {
        audienceService.add(audience);
        return audience;
    }

    @Operation(summary = "Get the audience by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the audience", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Audience.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Audience not found", content = @Content) })
    @GetMapping("/{id}")
    public Audience one(@Parameter(description = "id of audience") @PathVariable int id) throws ServiceException {
        return audienceService.get(id);
    }

    @Operation(summary = "Delete the audience")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Audience deleted"),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "id of audience") @PathVariable int id) throws ServiceException {
        audienceService.delete(id);
    }

    @Operation(summary = "Update the audience")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audience updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Audience.class)) }),
            @ApiResponse(responseCode = "404", description = "No audience exists with given id", content = @Content) })
    @PutMapping(value = "/{id}", consumes = "application/json")
    public Audience update(@RequestBody @Valid Audience audience,
            @Parameter(description = "id of audience") @PathVariable int id) throws ServiceException {
        audience.setId(id);
        audienceService.update(audience);
        return audience;
    }
}