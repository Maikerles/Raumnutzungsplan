package com.sae.fds.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.sae.fds.domain.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sae.fds.domain.Account;
import com.sae.fds.domain.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Serializable> {
	List<Event> findAll();
	List<Event> findByAccount(Account account);
	List<Event> findByTable(Table table);
	Event findById(Long id);
	
	void delete(Event event);
	
    @Query("select c from Event c " +
	         "where c.start = :start")
	List<Event> findByStartDate(
            @Param("start") Date start);
    
    @Query("select d from Event d " +
	         "where d.start = :start and d.table = :table")
	List<Event> findByStartDateAndRoom(
           @Param("start") Date start,
           @Param("table") Table table);

	@Query("select b from Event b " +
	         "where b.start between ?1 and ?2 and b.end between ?1 and ?2 and b.table.id = ?3")
	 List<Event> findByDatesBetween(Date start, Date end, Long id);
	
	@Query("select b from Event b " +
	         "where b.start between ?1 and ?2 or b.end between ?1 and ?2 and b.table.id = ?3")
	 List<Event> findByDatesBetweenOrOr(Date start, Date end, Long id);
	
	@Query("select b from Event b " +
	         "where ?2 > b.start and ?1 < b.end and b.table.id = ?3")
	 List<Event> findByDatesBetweenOr(Date start, Date end, Long id);
	
	@Query("select b from Event b " +
	         "where ?2 > b.start and ?1 < b.end and b.table.id = ?3 and b.id = ?4")
	 List<Event> findByDatesBetweenOrAnd(Date start, Date end, Long id, Long roomId);
}