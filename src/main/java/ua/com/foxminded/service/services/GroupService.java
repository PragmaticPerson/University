package ua.com.foxminded.service.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.dao.repository.GroupRepository;
import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.faculty.Group;

@Service
public class GroupService implements ModelsService<Group> {
    private GroupRepository groupRepos;

    @Autowired
    public GroupService(GroupRepository groupRepos) {
        this.groupRepos = groupRepos;
    }

    @Override
    public Group get(int id) throws ServiceException {
        try {
            return groupRepos.getOne(id);
        } catch (RuntimeException e) {
            String message;
            if (e.getCause() instanceof EntityNotFoundException) {
                message = String.format("Wrong id %d", id);
                throw new IllegalArgumentException(message, e);
            }
            message = String.format("Unable to get group with id %d", id);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public List<Group> getAll() throws ServiceException {
        try {
            return groupRepos.findAll();
        } catch (RuntimeException e) {
            throw new ServiceException("Unable to get all groups", e);
        }
    }

    @Override
    public void add(Group entity) throws ServiceException {
        try {
            groupRepos.save(entity);
        } catch (RuntimeException e) {
            String message = String.format("Unable to add group %s", entity);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public void delete(int id) throws ServiceException {
        try {
            groupRepos.softDelete(id);
        } catch (RuntimeException e) {
            String message = String.format("Unable to delete group with id %d", id);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public void update(Group entity) throws ServiceException {
        try {
            groupRepos.save(entity);
        } catch (RuntimeException e) {
            String message = String.format("Unable to update group: %s", entity);
            throw new ServiceException(message, e);
        }
    }
}