package com.sae.fds.repository;

import java.io.Serializable;


import java.util.List;

import com.sae.fds.domain.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*Sql statements für Arbeitsplatz werden generiert*/

@Repository
public interface UnitRepository extends JpaRepository<Unit, Serializable> {
	
	Unit findById(Long id);
	Unit findByName(String name);
	Unit findByBemerkung(String bemerkung);
        List<Unit> findAllByOrderByNameAsc();
    
    @Modifying
    @Transactional
    @Query("update Unit u set u.name = :name, u.bemerkung = :bemerkung "
            + "where u.id = :id ")
    int updateUnit(
            @Param("id") Long id,
            @Param("name") String name,
    		@Param("bemerkung") String bemerkung);
}