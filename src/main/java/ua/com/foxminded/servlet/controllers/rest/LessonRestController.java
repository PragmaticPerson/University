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
import ua.com.foxminded.service.models.timetable.Lesson;
import ua.com.foxminded.service.models.timetable.Weekdays;
import ua.com.foxminded.service.services.LessonService;

@RestController
@RequestMapping("/webapp/lessons")
public class LessonRestController {
    private LessonService lessonService;

    @Autowired
    public LessonRestController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @Operation(summary = "Gets all lessons for current group")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the lessons", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Lesson.class))) }),
            @ApiResponse(responseCode = "404", description = "No lessons found", content = @Content) })
    @GetMapping("/groups/{id}")
    public List<Lesson> allForGroup(@Parameter(description = "Group id to search its lessons") @PathVariable int id)
            throws ServiceException {
        return lessonService.getLessonsByGroup(id);
    }

    @Operation(summary = "Gets all lessons for current group at current day")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the lessons", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Lesson.class))) }),
            @ApiResponse(responseCode = "404", description = "No lessons found", content = @Content) })
    @GetMapping("/groups/{id}/{day}")
    public List<Lesson> allForGroupByDay(
            @Parameter(description = "Group id to search its lessons") @PathVariable int id,
            @Parameter(description = "Day name to search its lessons") @PathVariable String day)
            throws ServiceException {
        return lessonService.getLessonsByGroupAndDay(id, Weekdays.valueOf(day.toUpperCase()));
    }

    @Operation(summary = "Gets all lessons for current teacher")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the lessons", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Lesson.class))) }),
            @ApiResponse(responseCode = "404", description = "No lessons found", content = @Content) })
    @GetMapping("/teachers/{id}")
    public List<Lesson> allForTeacher(@Parameter(description = "Teacher id to search its lessons") @PathVariable int id)
            throws ServiceException {
        return lessonService.getLessonsByTeacher(id);
    }

    @Operation(summary = "Gets all lessons for current teacher at current day")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the lessons", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Lesson.class))) }),
            @ApiResponse(responseCode = "404", description = "No lessons found", content = @Content) })
    @GetMapping("/teachers/{id}/{day}")
    public List<Lesson> allForTeacherByDay(
            @Parameter(description = "Teacher id to search its lessons") @PathVariable int id,
            @Parameter(description = "Day name to search its lessons") @PathVariable String day)
            throws ServiceException {
        return lessonService.getLessonsByTeacherAndDay(id, Weekdays.valueOf(day.toUpperCase()));
    }

    @Operation(summary = "Create a lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lesson created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Lesson.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Lesson add(@RequestBody @Valid Lesson lesson) throws ServiceException {
        lessonService.add(lesson);
        return lesson;
    }

    @Operation(summary = "Get the lesson by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the lesson", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Lesson.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lesson not found", content = @Content) })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Lesson one(@Parameter(description = "id of lesson") @PathVariable long id) throws ServiceException {
        return lessonService.getLesson(id);
    }

    @Operation(summary = "Delete a lesson")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Lesson deleted"),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "id of lesson") @PathVariable int id) throws ServiceException {
        lessonService.delete(id);
    }

    @Operation(summary = "Update the lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Lesson.class)) }),
            @ApiResponse(responseCode = "404", description = "No lesson exists with given id", content = @Content) })
    @PutMapping(value = "/{id}", consumes = "application/json")
    public Lesson update(@RequestBody @Valid Lesson lesson,
            @Parameter(description = "id of lesson") @PathVariable int id) throws ServiceException {
        lesson.setId(id);
        lessonService.update(lesson);
        return lesson;
    }

}
