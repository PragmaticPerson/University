package ua.com.foxminded.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.service.models.people.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Override
    @Query("SELECT s FROM Student s WHERE s.deleted=false ORDER BY s.id ASC")
    List<Student> findAll();

    @Query("select s from Student s where s.deleted=true ORDER BY s.id ASC")
    public List<Student> recycleBin();

    @Modifying
    @Transactional
    @Query("UPDATE Student s SET s.deleted=true WHERE s.id=:id")
    void softDelete(@Param("id") int id);
    
    int countByGroupId(int groupId);
}
