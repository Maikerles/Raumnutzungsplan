package com.sae.fds.repository;

import java.io.Serializable;

import com.sae.fds.domain.UserAgent;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAgentRepository extends JpaRepository<UserAgent, Serializable>{    
}
    

