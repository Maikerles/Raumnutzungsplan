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

@Repository
public interface TableRepository extends JpaRepository<Table, Serializable> {
	
	Table findById(Long id);
	Table findByName(String name);
	List<Table> findByUnit(Unit unit);
	
	@Query("select ro from table_entity ro " +
	         "where ro.name = :name and ro.unit = :unit")
    Table findByNameAndUnit(
			@Param("name") String name,  
			@Param("unit") Unit unit);
    
    @Modifying
    @Transactional
    @Query("update table_entity r set r.name = :name, r.unit = :unit "
            + "where r.id = :id")
    int updateTable(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("unit") Unit unit);
}
