package com.sae.fds.initData;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.sae.fds.service.AccountService;
import com.sae.fds.service.TableService;
import com.sae.fds.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sae.fds.domain.Account;
import com.sae.fds.domain.Table;
import com.sae.fds.domain.Unit;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InitDefaultData {
	
	@Autowired
	private AccountService accountService;

	@Autowired
	private UnitService unitService;
	
	@Autowired
	private TableService tableService;
	
	@PostConstruct
	private void initDefaultData() {
		log.debug("Init default start");
		///*


		
		List<Account> accounts = new ArrayList<Account>();
		Account account;
		
		account = new Account();
		account.setUserName("admin");
		account.setPassword("admin");
		account.setEmail("admin@admin.com");
		account.setFirstName("Admin");
		account.setLastName("Admin");
		account.setRole("ROLE_ADMIN");
		account.setCompanyName("T-Systems");

		accounts.add(account);
		
		account = new Account();
		account.setUserName("user");
		account.setPassword("user");
		account.setEmail("user@user.com");
		account.setFirstName("User");
		account.setLastName("User");
		account.setRole("ROLE_USER");
		account.setCompanyName("T-Systems");

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
			
			table = new Table();
			table.setName("Table 1");
			table.setPosition("Tuer-Rechts");

			table.setUnit(unitService.findByName("Room A"));
			tables.add(table);
			
			table = new Table();
			table.setName("Table 2");
			table.setPosition("Fenster-Links");

			table.setUnit(unitService.findByName("Room A"));
			tables.add(table);
	
			table = new Table();
			table.setName("Table 3");
			table.setPosition("Fenster-Rechts");
			table.setUnit(unitService.findByName("Room B"));
			tables.add(table);
			
			for(Table r : tables) {
				tableService.add(r);
			}
		}
		catch(Exception e){
			log.debug("Section works only on first run on db creation");
		}
		//*/
		log.debug("Init default done");
	}
}