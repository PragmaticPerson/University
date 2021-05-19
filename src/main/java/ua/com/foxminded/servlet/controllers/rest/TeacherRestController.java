package ua.com.foxminded.servlet.controllers.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
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
import ua.com.foxminded.service.models.people.Teacher;
import ua.com.foxminded.service.services.TeacherService;

@RestController
@RequestMapping("/webapp/teachers")
public class TeacherRestController {
    private TeacherService teacherService;

    @Autowired
    public TeacherRestController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Operation(summary = "Gets all avaliable teachers")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the teachers", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Teacher.class))) }),
            @ApiResponse(responseCode = "404", description = "No teachers found", content = @Content) })
    @GetMapping
    public List<Teacher> all() throws ServiceException {
        return teacherService.getAll();
    }

    @Operation(summary = "Create an teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Teacher created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Teacher.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Teacher> add(@RequestBody @Valid Teacher group) throws ServiceException {
        teacherService.add(group);
        String linkToLessons = String.format("/lessons/teachers/%d/{day}", group.getId());
        return EntityModel.of(group, Link.of(linkToLessons, "lessons"));
    }

    @Operation(summary = "Get the teacher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the teacher", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Teacher.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Teacher not found", content = @Content) })
    @GetMapping("/{id}")
    public EntityModel<Teacher> one(@Parameter(description = "id of teacher") @PathVariable int id)
            throws ServiceException {
        String linkToLessons = String.format("/lessons/teachers/%d/{day}", id);
        return EntityModel.of(teacherService.get(id), Link.of(linkToLessons, "lessons"));
    }

    @Operation(summary = "Delete the teacher")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Teacher deleted"),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "id of teacher") @PathVariable int id) throws ServiceException {
        teacherService.delete(id);
    }

    @Operation(summary = "Update the teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Teacher.class)) }),
            @ApiResponse(responseCode = "404", description = "No teacher exists with given id", content = @Content) })
    @PutMapping(value = "/{id}", consumes = "application/json")
    public EntityModel<Teacher> update(@RequestBody @Valid Teacher group,
            @Parameter(description = "id of teacher") @PathVariable int id) throws ServiceException {
        group.setId(id);
        teacherService.update(group);
        String linkToLessons = String.format("/lessons/teachers/%d/{day}", id);
        return EntityModel.of(group, Link.of(linkToLessons, "lessons"));
    }
}
