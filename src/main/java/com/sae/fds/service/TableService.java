package com.sae.fds.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import com.sae.fds.domain.Table;
import com.sae.fds.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sae.fds.domain.Unit;

/* Bearbeitung der Daten */

@Service
public class TableService {
	@Autowired
	private TableRepository tableRepository;
	
	@Autowired
	private EventService eventService;

	public Table findById(Long id) {
		Table table = tableRepository.findById(id);
		return table;
	}

	public Table findByName(String name) {
		Table table = tableRepository.findByName(name);
		return table;
	}
	
	public Table findByPosition(String position) {
		Table table = tableRepository.findByPosition(position);
		return table;
	}
	public List<Table> findByUnit(Unit unit) {
		List<Table> tables = tableRepository.findByUnit(unit);
		return tables;
	}
	
	public List<Table> findAllByOrderByNameAsc(Unit unit) {
		List<Table> tables = tableRepository.findAllByOrderByNameAsc();
		return tables;
	}
	public List<Table> list() {
		return tableRepository.findAll();
	}
	
    public Boolean delete(Long id) {
        this.tableRepository.deleteById(id);
        return true;
    }
    
    public Table add(Table table) {
    	if (this.tableRepository.findByNameAndUnit(table.getName(), table.getUnit(), table.getPosition()) == null) {
    		this.tableRepository.save(table);
    		return table;
    	} else {
    		return null;
    	}
    }
    
    public Table update(Table table) {
        return update(table.getId(), table);
    }
    
    public Table update(Long id, Table newData) {
    	
    	String checkName = newData.getName();
    	String checkPosition = newData.getPosition();
    	Unit checkUnit = newData.getUnit();
    	Long checkId = id;
    	
    	Table check = this.tableRepository.findByNameAndUnit(newData.getName(), newData.getUnit(),newData.getPosition());
    	if (check != null) {
    		checkName = check.getName();
    		checkId = check.getId();
    		checkPosition = check.getPosition();
    		checkUnit = check.getUnit();
    	}

    	if ( checkName.equals(newData.getName()) || checkId.equals(id) || checkPosition.equals(newData.getPosition()) || checkUnit.equals(newData.getUnit())) {
            this.tableRepository.updateTable(
                    id, 
                    newData.getName(),
                    newData.getUnit(),
                    newData.getPosition());          
            return newData;
    	} else {
    		return null;
    	}
    }
    
	public List<List<String>> listTable() {
		List<Table> tables = tableRepository.findAllByOrderByNameAsc();
	    List<List<String>> roomsList = new LinkedList<List<String>>();
	   /* 
	    Comparator<Table> comparator = new Comparator<Table>() {
		    @Override
		    public int compare(Table left, Table right) {
		        return (int) (left.getId() - right.getId());
		    }
		};
		Collections.sort(tables, comparator);
	    */
	    
	    
	    
	    
	    for (Table table : tables) {
	    	List<String> roomData = new ArrayList<String>();
	    	
	    	roomData.add("<a style=\"color: #f9b012\" href=\"/room/edit/" + table.getId() + "\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\"><path d=\"M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25z\"></path><path d=\"M20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z\"></path></svg></a>");
	    	roomData.add("<a style=\"color: #f9b012\" onClick=\"return confirm('Soll der Datensatz wirklich gelöscht werden?');\" href=\"/room/delete?id=" + table.getId() + "\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\"><path d=\"M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12z\"></path><path d=\"M19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z\"></path></svg></a></a>");
	    	roomData.add(table.getUnit().getName());
	    	roomData.add(table.getName());
	    	roomData.add(table.getPosition());
	    		    	
	    		    	
	    	roomsList.add(roomData);
	    }
		return roomsList;
	}
	
	public List<List<String>> listTable(Long id) {
		List<Table> tables = tableRepository.findAllByOrderByNameAsc();
	    List<List<String>> roomsList = new LinkedList<List<String>>();
	    
	  /*  Comparator<Table> comparator = new Comparator<Table>() {
		    @Override
		    public int compare(Table left, Table right) {
		        return (int) (left.getId() - right.getId());
		    }
		};
		Collections.sort(tables, comparator);*/
	    
	    for (Table table : tables) {
	    	List<String> roomData = new ArrayList<String>();
	    	
	    	if (table.getUnit().getId().equals(id)) {
	    	
		    	roomData.add("<a style=\"color: #f9b012\" href=\"/book/room/" + table.getId() + "\"><svg fill=\"#000000\" height=\"32\" viewBox=\"0 0 32 32\" width=\"32\" xmlns=\"http://www.w3.org/2000/svg\"><path d=\"M8.59 16.34l4.58-4.59-4.58-4.59L10 5.75l6 6-6 6z\"/><path d=\"M0-.25h24v24H0z\" fill=\"none\"/></svg></a>");
				roomData.add(table.getName());
				roomData.add(table.getPosition());

		    	//int count = eventService.countInRoom(table);
		    	roomData.add("");
		    	
		    	roomsList.add(roomData);
	    	}
	    }
		return roomsList;
	}
	
	public List<List<String>> listTablePublic(String name) {
		List<Table> tables = tableRepository.findAll();
	    List<List<String>> roomsList = new LinkedList<List<String>>();
	    
	    Comparator<Table> comparator = new Comparator<Table>() {
		    @Override
		    public int compare(Table left, Table right) {
		        return (int) (left.getId() - right.getId());
		    }
		};
		Collections.sort(tables, comparator);
	    
	    for (Table table : tables) {
	    	List<String> roomData = new ArrayList<String>();
			roomData.add("<a style=\"color: #f9b012\" href=\"/calendar/table/" + table.getId() + "\"><svg fill=\"#000000\" height=\"32\" viewBox=\"0 0 32 32\" width=\"32\" xmlns=\"http://www.w3.org/2000/svg\"><path d=\"M8.59 16.34l4.58-4.59-4.58-4.59L10 5.75l6 6-6 6z\"/><path d=\"M0-.25h24v24H0z\" fill=\"none\"/></svg></a>");
			roomData.add(table.getName());
			roomData.add(String.valueOf(table.getUnit().getName()));
			//int count = eventService.countInRoom(table);
			roomData.add("");

	    	
	    	roomsList.add(roomData);

	    }
		return roomsList;
	}
	
	public List<Table> listExport() {
		List<Table> tables = tableRepository.findAll();
		
	    Comparator<Table> comparator = new Comparator<Table>() {
		    @Override
		    public int compare(Table left, Table right) {
		        return (int) (left.getId() - right.getId());
		    }
		};
		Collections.sort(tables, comparator);
		
		return tables;
	}
	
	public int countInUnit(Unit unit) {
		return this.tableRepository.findByUnit(unit).size();
	}
	
	public List<Table> roomUnitList(Unit unit) {
		return this.tableRepository.findByUnit(unit);
	}
}
