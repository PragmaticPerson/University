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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.subject.Subject;
import ua.com.foxminded.service.services.SubjectService;

@RestController
@RequestMapping("/webapp/subjects")
public class SubjectRestController {
    private SubjectService subjectService;

    @Autowired
    public SubjectRestController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Operation(summary = "Gets all avaliable subjects")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the subjects", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Subject.class))) }),
            @ApiResponse(responseCode = "404", description = "No subjects found", content = @Content) })
    @GetMapping
    public List<Subject> all() throws ServiceException {
        return subjectService.getAll();
    }

    @Operation(summary = "Create an subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subject created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Subject.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Subject add(@RequestBody @Valid Subject subject) throws ServiceException {
        subjectService.add(subject);
        return subject;
    }

    @Operation(summary = "Get the subject by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the subject", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Subject.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content) })
    @GetMapping("/{id}")
    public Subject one(@Parameter(description = "id of subject") @PathVariable int id) throws ServiceException {
        return subjectService.get(id);
    }

    @Operation(summary = "Delete the subject")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Subject deleted"),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "id of subject") @PathVariable int id) throws ServiceException {
        subjectService.delete(id);
    }

    @Operation(summary = "Update the subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Subject.class)) }),
            @ApiResponse(responseCode = "404", description = "No subject exists with given id", content = @Content) })
    @PutMapping(value = "/{id}", consumes = "application/json")
    public Subject update(@RequestBody @Valid Subject subject,
            @Parameter(description = "id of subject") @PathVariable int id) throws ServiceException {
        subject.setId(id);
        subjectService.update(subject);
        return subject;
    }
}
