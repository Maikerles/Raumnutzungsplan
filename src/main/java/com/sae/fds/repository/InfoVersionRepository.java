package com.sae.fds.repository;

import java.io.Serializable;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sae.fds.domain.InfoVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/*Sql statements für Info werden generiert*/

@Repository
public interface InfoVersionRepository extends JpaRepository<InfoVersion, Serializable> {

	InfoVersion findById(Long id);
	InfoVersion findByInfo(String info);

	@Modifying
	@Transactional
	@Query("update InfoVersion i set i.info = :info "
			+ "where i.id = :id ")
	int updateInfoVersion(
			@Param("id") Long id,
			@Param("info") String info);
	
}
