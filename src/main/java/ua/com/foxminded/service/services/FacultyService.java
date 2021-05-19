package ua.com.foxminded.service.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.dao.repository.FacultyRepository;
import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.faculty.Faculty;

@Service
public class FacultyService implements ModelsService<Faculty> {
    private FacultyRepository facultyRepos;

    @Autowired
    public FacultyService(FacultyRepository facultyRepos) {
        this.facultyRepos = facultyRepos;
    }

    @Override
    public Faculty get(int id) throws ServiceException {
        try {
            return facultyRepos.getOne(id);
        } catch (RuntimeException e) {
            String message;
            if (e.getCause() instanceof EntityNotFoundException) {
                message = String.format("Wrong id %d", id);
                throw new IllegalArgumentException(message, e);
            }
            message = String.format("Unable to get faculty with id %d", id);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public List<Faculty> getAll() throws ServiceException {
        try {
            return facultyRepos.findAll();
        } catch (RuntimeException e) {
            throw new ServiceException("Unable to get all faculties", e);
        }
    }

    @Override
    public void add(Faculty entity) throws ServiceException {
        try {
            facultyRepos.save(entity);
        } catch (RuntimeException e) {
            String message = String.format("Unable to add faculty: %s", entity);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public void delete(int id) throws ServiceException {
        try {
            facultyRepos.softDelete(id);
        } catch (RuntimeException e) {
            String message = String.format("Unable to delete faculty with id %d", id);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public void update(Faculty entity) throws ServiceException {
        try {
            facultyRepos.save(entity);
        } catch (RuntimeException e) {
            String message = String.format("Unable to update faculty: %s", entity);
            throw new ServiceException(message, e);
        }
    }
}
