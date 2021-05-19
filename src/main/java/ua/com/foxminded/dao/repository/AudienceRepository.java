package ua.com.foxminded.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.service.models.audience.Audience;

public interface AudienceRepository extends JpaRepository<Audience, Integer> {

    @Override
    @Query("SELECT a FROM Audience a WHERE a.deleted=false ORDER BY a.number ASC")
    List<Audience> findAll();

    @Query("select a from Audience a where a.deleted=true ORDER BY a.id ASC")
    public List<Audience> recycleBin();

    @Modifying
    @Transactional
    @Query("UPDATE Audience a SET a.deleted=true WHERE a.id=:id")
    public void softDelete(@Param("id") int id);
    
    public int countByNumberAndIdNot(int number, int id);
}
