package ua.com.foxminded.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.dao.repository.FacultyRepository;
import ua.com.foxminded.service.models.faculty.Faculty;
import ua.com.foxminded.validator.annotations.FacultyConstraint;

@Component
public class FacultyValidator implements ConstraintValidator<FacultyConstraint, Faculty> {

    private FacultyRepository facultyRepos;

    @Autowired
    public void setFacultyRepos(FacultyRepository facultyRepos) {
        this.facultyRepos = facultyRepos;
    }

    @Override
    public boolean isValid(Faculty value, ConstraintValidatorContext context) {
        if (facultyRepos.countByNameAndIdNot(value.getName(), value.getId()) > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Faculty with this name already exists")
                    .addConstraintViolation();
        } else if (facultyRepos.countByDeanFirstNameAndDeanLastNameAndIdNot(value.getDeanFirstName(),
                value.getDeanLastName(), value.getId()) > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("This dean can't lead more than one faculty")
                    .addConstraintViolation();
        } else {
            return true;
        }
        return false;
    }

}
