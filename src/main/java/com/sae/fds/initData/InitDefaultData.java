package com.sae.fds.initData;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.sae.fds.service.AccountService;
import com.sae.fds.service.InfoVersionService;
import com.sae.fds.service.TableService;
import com.sae.fds.service.UnitService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.squareup.okhttp.Request;
import com.sae.fds.domain.Account;
import com.sae.fds.domain.InfoVersion;
import com.sae.fds.domain.Table;
import com.sae.fds.domain.Unit;
import com.sae.fds.domain.UserAgent;
import com.sae.fds.repository.UserAgentRepository;

import lombok.extern.slf4j.Slf4j;
import java.io.File;  

@Component
@Slf4j
public class InitDefaultData {

	@Autowired
	private AccountService accountService;

	@Autowired
	private UnitService unitService;

	@Autowired
	private TableService tableService;

	@Autowired
	private UserAgentRepository agentRepository;
	
	@Autowired
	private InfoVersionService infoVersionService;

	

/* Testdaten werden erstellt*/

	@PostConstruct
	private void initDefaultData() throws CsvValidationException, IOException {
		log.debug("Init default start");

				
		List<Account> accounts = new ArrayList<Account>();
		Account account;
		
		account = new Account();
		account.setUserName("admin");
		account.setPassword("admin");
		account.setEmail("admin@admin.com");
		account.setFirstName("Admin");
		account.setLastName("Admin");
		account.setRole("ROLE_ADMIN");

		accounts.add(account);
		
		account = new Account();
		account.setUserName("user");
		account.setPassword("user");
		account.setEmail("user@user.com");
		account.setFirstName("User");
		account.setLastName("User");
		account.setRole("ROLE_USER");

		accounts.add(account);
		
		for(Account a : accounts) {        
			accountService.register(a);
		}
		try {
			List<Unit> units = new ArrayList<Unit>();
			Unit unit;
			
			unit = new Unit();
			unit.setName("Room A");

			units.add(unit);
			
			unit = new Unit();
			unit.setName("Room B");
			units.add(unit);
			
			for(Unit u : units) {        
				unitService.add(u);
			}
			
			List<Table> tables = new ArrayList<Table>();
			Table table;
			
		/*	table = new Table();
			table.setName("5.001-001");
			table.setPosition("Tuer-Rechts");

			table.setUnit(unitService.findByName("5.001"));
			tables.add(table);
			
			table = new Table();
			table.setName("5.001-002");
			table.setPosition("Fenster-Links");

			table.setUnit(unitService.findByName("5.001"));
			tables.add(table);
	
			table = new Table();
			table.setName("5.002-001");
			table.setPosition("Fenster-Rechts");
			table.setUnit(unitService.findByName("5.002"));
			tables.add(table);
			*/
			for(Table r : tables) {
				tableService.add(r);
			}
			
			List<InfoVersion> infos = new ArrayList<InfoVersion>();
			InfoVersion info;
			info = new InfoVersion();
			info.setInfo("SAE Projekt - Bookingtool");
			infos.add(info);
			for(InfoVersion i : infos) {        
				infoVersionService.add(i);
			}

		}
		catch(Exception e){
			log.debug("Section works only on first run on db creation");
		}
		//*/
		log.debug("Init default done");
	}
}
