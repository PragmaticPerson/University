package ua.com.foxminded.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.dao.repository.GroupRepository;
import ua.com.foxminded.service.models.faculty.Group;
import ua.com.foxminded.validator.annotations.GroupConstraint;

@Component
public class GroupValidator implements ConstraintValidator<GroupConstraint, Group> {

    private GroupRepository groupRepos;

    @Autowired
    public void setGroupRepos(GroupRepository groupRepos) {
        this.groupRepos = groupRepos;
    }

    @Override
    public boolean isValid(Group value, ConstraintValidatorContext context) {
        if (groupRepos.countByNameAndIdNot(value.getName(), value.getId()) > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Group with this name already exists")
                    .addConstraintViolation();
        } else {
            return true;
        }
        return false;
    }

}
