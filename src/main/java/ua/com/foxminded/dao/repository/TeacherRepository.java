package ua.com.foxminded.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.service.models.people.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Override
    @Query("SELECT t FROM Teacher t WHERE t.deleted=false ORDER BY t.id ASC")
    List<Teacher> findAll();

    @Query("select t from Teacher t where t.deleted=true ORDER BY t.id ASC")
    public List<Teacher> recycleBin();

    @Modifying
    @Transactional
    @Query("UPDATE Teacher t SET t.deleted=true WHERE t.id=:id")
    void softDelete(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM teachers_subjects WHERE subject_id=:id", nativeQuery = true)
    void deleteAllSubjectsForTeachers(@Param("id") int id);
    
    int countByNameAndSurnameAndIdNot(String name, String surname, int id);
}
