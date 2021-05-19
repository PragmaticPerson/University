package ua.com.foxminded.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.service.models.faculty.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {

    @Override
    @Query("SELECT f FROM Faculty f WHERE f.deleted=false ORDER BY f.id ASC")
    List<Faculty> findAll();

    @Query("select f from Faculty f where f.deleted=true ORDER BY f.id ASC")
    public List<Faculty> recycleBin();

    @Modifying
    @Transactional
    @Query("UPDATE Faculty f SET f.deleted=true WHERE f.id=:id")
    void softDelete(@Param("id") int id);

    int countByNameAndIdNot(String name, int id);

    int countByDeanFirstNameAndDeanLastNameAndIdNot(String deanFirstName, String deanLastName, int id);
}
