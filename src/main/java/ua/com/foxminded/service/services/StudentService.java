package ua.com.foxminded.service.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.dao.repository.StudentRepository;
import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.people.Student;

@Service
public class StudentService implements ModelsService<Student> {
    private StudentRepository studentRepos;

    @Autowired
    public StudentService(StudentRepository studentRepos) {
        this.studentRepos = studentRepos;
    }

    @Override
    public Student get(int id) throws ServiceException {
        try {
            return studentRepos.getOne(id);
        } catch (RuntimeException e) {
            String message = String.format("Unable to get student with id %d", id);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public List<Student> getAll() throws ServiceException {
        try {
            return studentRepos.findAll();
        } catch (RuntimeException e) {
            throw new ServiceException("Unable to get all audiences", e);
        }
    }

    public void add(Student entity) throws ServiceException {
        try {
            studentRepos.save(entity);
        } catch (RuntimeException e) {
            String message = String.format("Unable to add audience: %s", entity);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public void delete(int id) throws ServiceException {
        try {
            studentRepos.softDelete(id);
        } catch (RuntimeException e) {
            String message = String.format("Unable to delete audience with id %d", id);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public void update(Student entity) throws ServiceException {
        try {
            studentRepos.save(entity);
        } catch (RuntimeException e) {
            String message = String.format("Unable to update student: %s", entity);
            throw new ServiceException(message, e);
        }
    }
}
