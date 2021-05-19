package ua.com.foxminded.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.dao.repository.AudienceRepository;
import ua.com.foxminded.service.models.audience.Audience;
import ua.com.foxminded.validator.annotations.AudienceConstraint;

@Service
public class AudienceValidator implements ConstraintValidator<AudienceConstraint, Audience> {

    private AudienceRepository audienceRepos;

    @Autowired
    public void setAudienceRepos(AudienceRepository audienceRepos) {
        this.audienceRepos = audienceRepos;
    }

    @Override
    public boolean isValid(Audience value, ConstraintValidatorContext context) {
        if (audienceRepos.countByNumberAndIdNot(value.getNumber(), value.getId()) > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Audience with this number already exists")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}