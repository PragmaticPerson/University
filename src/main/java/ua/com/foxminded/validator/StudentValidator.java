package ua.com.foxminded.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ua.com.foxminded.dao.repository.StudentRepository;
import ua.com.foxminded.service.models.people.Student;
import ua.com.foxminded.validator.annotations.StudentConstraint;

@Component
public class StudentValidator implements ConstraintValidator<StudentConstraint, Student> {

    private StudentRepository studentRepos;

    @Value("${group.maxStudents}")
    private int maxStudentCountInGroup;

    @Autowired
    public void setStudentRepos(StudentRepository studentRepos) {
        this.studentRepos = studentRepos;
    }

    @Override
    public boolean isValid(Student value, ConstraintValidatorContext context) {
        if (studentRepos.countByGroupId(value.getGroup().getId()) >= maxStudentCountInGroup) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "There is no empty space in group. Please, choose another group").addConstraintViolation();
            return false;
        }
        return true;
    }

}
