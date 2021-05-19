package ua.com.foxminded.service.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.dao.repository.AudienceRepository;
import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.audience.Audience;

@Service
public class AudienceService implements ModelsService<Audience> {
    private AudienceRepository audienceRepos;

    @Autowired
    public AudienceService(AudienceRepository audienceRepos) {
        this.audienceRepos = audienceRepos;
    }

    @Override
    public Audience get(int id) throws ServiceException {
        try {
            return audienceRepos.getOne(id);
        } catch (RuntimeException e) {
            String message;
            if (e.getCause() instanceof EntityNotFoundException) {
                message = String.format("Wrong id %d", id);
                throw new IllegalArgumentException(message, e);
            }
            message = String.format("Unable to get audience with id %d", id);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public List<Audience> getAll() throws ServiceException {
        try {
            return audienceRepos.findAll();
        } catch (RuntimeException e) {
            throw new ServiceException("Unable to get all audiences", e);
        }
    }

    @Override
    public void add(Audience entity) throws ServiceException {
        try {
            audienceRepos.save(entity);
        } catch (RuntimeException e) {
            String message = String.format("Unable to add audience: %s", entity);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public void delete(int id) throws ServiceException {
        try {
            audienceRepos.softDelete(id);
        } catch (RuntimeException e) {
            String message = String.format("Unable to delete audience with id %d", id);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public void update(Audience entity) throws ServiceException {
        try {
            audienceRepos.save(entity);
        } catch (RuntimeException e) {
            String message = String.format("Unable to update audience: %s", entity);
            throw new ServiceException(message, e);
        }
    }
}