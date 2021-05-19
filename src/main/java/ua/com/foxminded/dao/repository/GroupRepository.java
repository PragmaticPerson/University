package ua.com.foxminded.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.service.models.faculty.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Override
    @Query("SELECT g FROM Group g WHERE g.deleted=false ORDER BY g.id ASC")
    List<Group> findAll();

    @Query("select g from Group g where g.deleted=true ORDER BY g.id ASC")
    public List<Group> recycleBin();

    @Modifying
    @Transactional
    @Query("UPDATE Group g SET g.deleted=true WHERE g.id=:id")
    void softDelete(@Param("id") int id);

    int countByNameAndIdNot(String name, int id);
}
