package ua.com.foxminded.validator;

import java.util.StringJoiner;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.dao.repository.LessonRepository;
import ua.com.foxminded.dao.repository.TeacherRepository;
import ua.com.foxminded.service.models.timetable.Lesson;
import ua.com.foxminded.service.models.timetable.LessonNumber;
import ua.com.foxminded.service.models.timetable.Weekdays;
import ua.com.foxminded.validator.annotations.LessonConstraint;

@Component
public class LessonValidator implements ConstraintValidator<LessonConstraint, Lesson> {
    private LessonRepository lessonRepos;
    private TeacherRepository teacherRepos;

    @Autowired
    public void setLessonRepos(LessonRepository lessonRepos) {
        this.lessonRepos = lessonRepos;
    }

    @Autowired
    public void setTeacherRepos(TeacherRepository teacherRepos) {
        this.teacherRepos = teacherRepos;
    }

    @Override
    public boolean isValid(Lesson value, ConstraintValidatorContext context) {
        long lessonId = value.getId();
        int teacherId = value.getTeacher().getId();
        int groupId = value.getGroup().getId();
        int subjectId = value.getSubject().getId();
        int audienceId = value.getAudience().getId();
        Weekdays day = value.getDay();
        LessonNumber number = value.getLessonNumber();

        if (teacherRepos.getOne(teacherId).getSubjects().stream().filter(s -> s.getId() == subjectId).count() == 0) {
            StringJoiner joiner = new StringJoiner(", ", "Teacher doesn't teach this subject. Subjects: ", ".");
            teacherRepos.getOne(teacherId).getSubjects().stream().forEach(s -> joiner.add(s.getName()));
            setMessageToContext(joiner.toString(), context);

        } else if (lessonRepos.getLessonsByTeacherAndDay(teacherId, day).stream()
                .filter(l -> l.getLessonNumber().equals(number) && l.getId() != lessonId).count() > 0) {
            setMessageToContext("Teacher has a lesson at current time", context);

        } else if (lessonRepos.getLessonsByGroupAndDay(groupId, day).stream()
                .filter(l -> l.getLessonNumber().equals(number) && l.getId() != lessonId).count() > 0) {
            setMessageToContext("Group has a lesson at current time", context);

        } else if (lessonRepos.getLessonsByAudienceAndDay(audienceId, day).stream()
                .filter(l -> l.getLessonNumber().equals(number) && l.getId() != lessonId).count() > 0) {
            setMessageToContext("Audience has a lesson at current time", context);

        } else {
            return true;
        }
        return false;
    }

    private void setMessageToContext(String message, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
