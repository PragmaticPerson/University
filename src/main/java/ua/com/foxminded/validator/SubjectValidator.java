package ua.com.foxminded.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.dao.repository.SubjectRepository;
import ua.com.foxminded.service.models.subject.Subject;
import ua.com.foxminded.validator.annotations.SubjectConstraint;

@Component
public class SubjectValidator implements ConstraintValidator<SubjectConstraint, Subject> {

    private SubjectRepository subjectRepos;

    @Autowired
    public SubjectValidator(SubjectRepository subjectRepos) {
        this.subjectRepos = subjectRepos;
    }

    @Override
    public boolean isValid(Subject value, ConstraintValidatorContext context) {
        if (subjectRepos.countByNameAndIdNot(value.getName(), value.getId()) > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Subject with this name is already exists")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

}
