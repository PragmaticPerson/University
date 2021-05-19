package ua.com.foxminded.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.service.models.subject.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Override
    @Query("SELECT s FROM Subject s WHERE s.deleted=false ORDER BY s.id ASC")
    List<Subject> findAll();

    @Query("select s from Subject s where s.deleted=true ORDER BY s.id ASC")
    public List<Subject> recycleBin();

    @Modifying
    @Transactional
    @Query("UPDATE Subject s SET s.deleted=true WHERE s.id=:id")
    void softDelete(@Param("id") int id);
    
    int countByNameAndIdNot(String name, int id);
}
