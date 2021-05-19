package ua.com.foxminded.service.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.dao.repository.SubjectRepository;
import ua.com.foxminded.dao.repository.TeacherRepository;
import ua.com.foxminded.exception.ServiceException;
import ua.com.foxminded.service.models.people.Teacher;

@Service
public class TeacherService implements ModelsService<Teacher> {
    private TeacherRepository teacherRepos;
    private SubjectRepository subjectRepos;

    @Autowired
    public TeacherService(TeacherRepository teacherRepos, SubjectRepository subjectRepos) {
        this.teacherRepos = teacherRepos;
        this.subjectRepos = subjectRepos;
    }

    @Override
    public Teacher get(int id) throws ServiceException {
        try {
            return teacherRepos.getOne(id);
        } catch (RuntimeException e) {
            String message = String.format("Unable to get teacher with id %d", id);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public List<Teacher> getAll() throws ServiceException {
        try {
            return teacherRepos.findAll();
        } catch (RuntimeException e) {
            throw new ServiceException("Unable to get all teachers", e);
        }
    }

    @Override
    public void add(Teacher entity) throws ServiceException {
        try {
            teacherRepos.save(entity);
        } catch (RuntimeException e) {
            String message = String.format("Unable to add teacher: %s", entity);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public void delete(int id) throws ServiceException {
        try {
            teacherRepos.softDelete(id);
        } catch (RuntimeException e) {
            String message = String.format("Unable to delete teacher with id %d", id);
            throw new ServiceException(message, e);
        }
    }

    @Override
    public void update(Teacher entity) throws ServiceException {
        try {
            teacherRepos.save(entity);
        } catch (RuntimeException e) {
            String message = String.format("Unable to update teacher: %s", entity);
            throw new ServiceException(message, e);
        }
    }

    public void deleteSubject(int teacherId, int subjectId) throws ServiceException {
        try {
            Teacher teacher = get(teacherId);
            teacher.deleteSubject(subjectRepos.getOne(subjectId));
            update(teacher);
        } catch (RuntimeException e) {
            String message = String.format("Unable to delete subject %d from teacher: %d", subjectId, teacherId);
            throw new ServiceException(message, e);
        }
    }
}
