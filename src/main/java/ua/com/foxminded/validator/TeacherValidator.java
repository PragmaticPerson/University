package ua.com.foxminded.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.dao.repository.TeacherRepository;
import ua.com.foxminded.service.models.people.Teacher;
import ua.com.foxminded.validator.annotations.TeacherConstraint;

@Component
public class TeacherValidator implements ConstraintValidator<TeacherConstraint, Teacher> {

    private TeacherRepository teacherRepos;

    @Autowired
    public TeacherValidator(TeacherRepository teacherRepos) {
        this.teacherRepos = teacherRepos;
    }

    @Override
    public boolean isValid(Teacher value, ConstraintValidatorContext context) {
        if (teacherRepos.countByNameAndSurnameAndIdNot(value.getName(), value.getSurname(), value.getId()) > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Teacher with current name and surname already exists.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}