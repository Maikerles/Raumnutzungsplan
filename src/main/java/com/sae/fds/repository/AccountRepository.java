package com.sae.fds.repository;

import java.io.Serializable;

import com.sae.fds.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*Sql statements für account werden generiert*/

@Repository
public interface AccountRepository extends JpaRepository<Account, Serializable> {
    Account findOneByUserName(String name);
    Account findOneByEmail(String email);
    Account findOneByUserNameOrEmail(String username, String email);
    Account findOneByToken(String token);
    
    @Modifying
    @Transactional
    @Query("update Account a set a.email = :email, a.firstName = :firstName, "
            + "a.lastName = :lastName, a.projectName = :projectName, "
    		+ "a.role = :role, a.projectSupervisor = :projectSupervisor "
            + "where a.userName = :userName")
    int updateUser(
            @Param("userName") String userName, 
            @Param("email") String email,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("projectName") String projectName,
            @Param("role") String role, 
            @Param("projectSupervisor") String projectSupervisor);
    @Modifying
    @Transactional
    @Query("update Account a set a.email = :email, a.firstName = :firstName, "
            + "a.lastName = :lastName, a.projectName = :projectName, "
    		+ "a.password = :password, a.role = :role, a.projectSupervisor = :projectSupervisor "
            + "where a.userName = :userName")
    int updateUserWithPassword(
            @Param("userName") String userName, 
            @Param("email") String email,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("projectName") String projectName,
    		@Param("password") String password,
            @Param("role") String role,
            @Param("projectSupervisor") String projectSupervisor);
    
    @Modifying
    @Transactional
    @Query("update Account a set a.lastLogin = CURRENT_TIMESTAMP where a.userName = ?1")
    int updateLastLogin(String userName);
   
}