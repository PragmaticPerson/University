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
import ua.com.foxminded.service.models.people.Student;
import ua.com.foxminded.service.services.StudentService;

@RestController
@RequestMapping("/webapp/students")
public class StudentRestController {
    private StudentService studentService;

    @Autowired
    public StudentRestController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Gets all avaliable students")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the students", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Student.class))) }),
            @ApiResponse(responseCode = "404", description = "No students found", content = @Content) })
    @GetMapping
    public List<Student> all() throws ServiceException {
        return studentService.getAll();
    }

    @Operation(summary = "Create an student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Student.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Student add(@RequestBody @Valid Student student) throws ServiceException {
        studentService.add(student);
        return student;
    }

    @Operation(summary = "Get the student by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the student", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Student.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content) })
    @GetMapping("/{id}")
    public Student one(@Parameter(description = "id of student") @PathVariable int id) throws ServiceException {
        return studentService.get(id);
    }

    @Operation(summary = "Delete the student")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Student deleted"),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "id of student") @PathVariable int id) throws ServiceException {
        studentService.delete(id);
    }

    @Operation(summary = "Update the student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Student.class)) }),
            @ApiResponse(responseCode = "404", description = "No student exists with given id", content = @Content) })
    @PutMapping(value = "/{id}", consumes = "application/json")
    public Student update(@RequestBody @Valid Student student,
            @Parameter(description = "id of student") @PathVariable int id) throws ServiceException {
        student.setId(id);
        studentService.update(student);
        return student;
    }
}
