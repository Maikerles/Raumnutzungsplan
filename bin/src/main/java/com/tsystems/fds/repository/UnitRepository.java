package com.sae.fds.repository;

import java.io.Serializable;

import com.sae.fds.domain.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Serializable> {
	
	Unit findById(Long id);
	Unit findByName(String name);


    
    @Modifying
    @Transactional
    @Query("update Unit u set u.name = :name "
            + "where u.id = :id")
    int updateUnit(
            @Param("id") Long id,
            @Param("name") String name);
}