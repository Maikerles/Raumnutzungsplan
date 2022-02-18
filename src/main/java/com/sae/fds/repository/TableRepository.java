package com.sae.fds.repository;

import java.io.Serializable;

import java.util.List;

import com.sae.fds.domain.Table;
import com.sae.fds.domain.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*Sql statements für Tables werden generiert*/

@Repository
public interface TableRepository extends JpaRepository<Table, Serializable> {
	
	Table findById(Long id);
	Table findByName(String name);
	Table findByPosition(String position);
	List<Table> findByUnit(Unit unit);
	List<Table> findAllByOrderByNameAsc();



	@Query("select ro from table_entity ro " +
	         "where ro.name = :name and ro.unit = :unit and ro.position = :position")
    Table findByNameAndUnit(
			@Param("name") String name,  
			@Param("unit") Unit unit,
			@Param("position") String position);
    
    @Modifying
    @Transactional
    @Query("update table_entity r set r.name = :name, r.unit = :unit, r.position = :position "
            + "where r.id = :id")
    int updateTable(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("unit") Unit unit,
            @Param("position") String position);
}
