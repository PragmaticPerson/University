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
import ua.com.foxminded.service.models.faculty.Faculty;
import ua.com.foxminded.service.services.FacultyService;

@RestController
@RequestMapping("/webapp/faculties")
public class FacultyRestController {
    private FacultyService facultyService;

    @Autowired
    public FacultyRestController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @Operation(summary = "Gets all avaliable faculties")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the faculties", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Faculty.class))) }),
            @ApiResponse(responseCode = "404", description = "No faculties found", content = @Content) })
    @GetMapping
    public List<Faculty> all() throws ServiceException {
        return facultyService.getAll();
    }

    @Operation(summary = "Create a faculty")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Faculty created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Faculty.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Faculty add(@RequestBody @Valid Faculty faculty) throws ServiceException {
        facultyService.add(faculty);
        return faculty;
    }

    @Operation(summary = "Get the faculty by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the faculty", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Faculty.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Faculty not found", content = @Content) })
    @GetMapping("/{id}")
    public Faculty one(@Parameter(description = "id of faculty") @PathVariable int id) throws ServiceException {
        return facultyService.get(id);
    }

    @Operation(summary = "Delete the faculty")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Faculty deleted"),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "id of faculty") @PathVariable int id) throws ServiceException {
        facultyService.delete(id);
    }

    @Operation(summary = "Update the faculty")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Faculty updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Faculty.class)) }),
            @ApiResponse(responseCode = "404", description = "No faculty exists with given id", content = @Content) })
    @PutMapping(value = "/{id}", consumes = "application/json")
    public Faculty update(@RequestBody @Valid Faculty faculty,
            @Parameter(description = "id of faculty") @PathVariable int id) throws ServiceException {
        faculty.setId(id);
        facultyService.update(faculty);
        return faculty;
    }
}
