package ua.com.foxminded.service.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.dao.repository.SubjectRepository;
import ua.com.foxminded.dao.repository.TeacherRepository;
import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.subject.Subject;

@Service
public class SubjectService implements ModelsService<Subject> {
    private SubjectRepository subjectRepos;
    private TeacherRepository teacherRepos;

    @Autowired
    public SubjectService(SubjectRepository subjectRepos, TeacherRepository teacherRepos) {
        this.subjectRepos = subjectRepos;
        this.teacherRepos = teacherRepos;
    }

    @Override
    public Subject get(int id) throws ServiceException {
        try {
            return subjectRepos.getOne(id);
        } catch (RuntimeException e) {
            String message = String.format("Unable to get subject with id %d", id);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public List<Subject> getAll() throws ServiceException {
        try {
            return subjectRepos.findAll();
        } catch (RuntimeException e) {
            throw new ServiceException("Unable to get all subjects", e);
        }
    }

    @Override
    public void add(Subject entity) throws ServiceException {
        try {
            subjectRepos.save(entity);
        } catch (RuntimeException e) {
            String message = String.format("Unable to add subject: %s", entity);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public void delete(int id) throws ServiceException {
        try {
            teacherRepos.deleteAllSubjectsForTeachers(id);
            subjectRepos.softDelete(id);
        } catch (RuntimeException e) {
            String message = String.format("Unable to delete subject with id %d", id);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public void update(Subject entity) throws ServiceException {
        try {
            subjectRepos.save(entity);
        } catch (RuntimeException e) {
            String message = String.format("Unable to update subject: %s", entity);
            throw new ServiceException(message, e);
        }
    }
}
