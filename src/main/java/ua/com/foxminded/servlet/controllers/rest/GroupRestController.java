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
import ua.com.foxminded.service.models.faculty.Group;
import ua.com.foxminded.service.services.GroupService;

@RestController
@RequestMapping("/webapp/groups")
public class GroupRestController {
    private GroupService groupService;

    @Autowired
    public GroupRestController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Operation(summary = "Gets all avaliable groups")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the groups", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Group.class))) }),
            @ApiResponse(responseCode = "404", description = "No groups found", content = @Content) })
    @GetMapping
    public List<Group> all() throws ServiceException {
        return groupService.getAll();
    }

    @Operation(summary = "Create an group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Group created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Group.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Group> add(@RequestBody @Valid Group group) throws ServiceException {
        groupService.add(group);
        String linkToLessons = String.format("/lessons/groups/%d/{day}", group.getId());
        return EntityModel.of(group, Link.of(linkToLessons, "lessons"));
    }

    @Operation(summary = "Get the group by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the group", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Group.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Group not found", content = @Content) })
    @GetMapping("/{id}")
    public EntityModel<Group> one(@Parameter(description = "id of group") @PathVariable int id)
            throws ServiceException {
        String linkToLessons = String.format("/lessons/groups/%d/{day}", id);
        return EntityModel.of(groupService.get(id), Link.of(linkToLessons, "lessons"));
    }

    @Operation(summary = "Delete the group")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Group deleted"),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "id of group") @PathVariable int id) throws ServiceException {
        groupService.delete(id);
    }

    @Operation(summary = "Update the group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Group.class)) }),
            @ApiResponse(responseCode = "404", description = "No group exists with given id", content = @Content) })
    @PutMapping(value = "/{id}", consumes = "application/json")
    public EntityModel<Group> update(@RequestBody @Valid Group group,
            @Parameter(description = "id of group") @PathVariable int id) throws ServiceException {
        group.setId(id);
        groupService.update(group);
        String linkToLessons = String.format("/lessons/groups/%d/{day}", id);
        return EntityModel.of(group, Link.of(linkToLessons, "lessons"));
    }
}