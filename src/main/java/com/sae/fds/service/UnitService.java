package com.sae.fds.service;

import java.util.ArrayList;


import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sae.fds.domain.Unit;
import com.sae.fds.repository.UnitRepository;
/* Bearbeitung der Daten */

@Service
public class UnitService {
	@Autowired
	private UnitRepository unitRepository;
	
	@Autowired
	private TableService tableService;
	
	public Unit findById(Long id) {
		Unit unit = unitRepository.findById(id);
		return unit;
	}
	
	public Unit findByName(String name) {
		Unit unit = unitRepository.findByName(name);
		return unit;
	}
	public Unit findByBemerkung(String bemerkung) {
		Unit unit = unitRepository.findByBemerkung(bemerkung);
		return unit;
	}
	public List<Unit> list() {
		return unitRepository.findAll();
	}
	
    public Boolean delete(Long id) {
        this.unitRepository.deleteById(id);
        return true;
    }

    public Unit add(Unit unit) {
    	if (this.unitRepository.findByName(unit.getName()) == null) {
    		this.unitRepository.save(unit);
    		return unit;
    	} else {
    		return null;
    	}
    }
    
    public Unit update(Unit unit) {
        return update(unit.getId(), unit);
    }
    
    public Unit update(Long id, Unit newData) {
    	
    	String checkName = newData.getName();
    	String checkBemerkung = newData.getBemerkung();
    	Long checkId = id;
    	
    	Unit check = this.unitRepository.findByName(newData.getName());
    	if (check != null) {
    		checkName = check.getName();
    		checkBemerkung = check.getBemerkung();
    		checkId = check.getId();
    	}

    	if ( checkName.equals(newData.getName()) || checkId.equals(id) || checkBemerkung.equals(newData.getBemerkung())) {
            this.unitRepository.updateUnit(
                    id, 
                    newData.getName(),
                    newData.getBemerkung());
            return newData;
    	} else {
    		return null;
    	}
    }
	
	public List<List<String>> listTable() {
		List<Unit> units = unitRepository.findAllByOrderByNameAsc();
	    List<List<String>> unitsList = new LinkedList<List<String>>();
		

	    // Comparator<Unit> comparator = new Comparator<Unit>() {
		//     @Override
		//     public int compare(Unit left, Unit right) {
		//         return (int) (left.getId() - right.getId());
		//     }
		// };
		// Collections.sort(units, comparator);
	    
	    for (Unit unit:units) {	    	
	    	List<String> unitData = new ArrayList<String>();
	    	
	    	unitData.add("<a style=\"color: #f9b012\" href=\"/unit/edit/" + unit.getId() + "\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\"><path d=\"M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25z\"></path><path d=\"M20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z\"></path></svg></a>");
			unitData.add("<a onClick=\"return confirm('Soll der Datensatz wirklich gelöscht werden?');\" style=\"color: #f9b012\" href=\"/unit/delete?id=" + unit.getId() + "\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\"><path d=\"M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12z\"></path><path d=\"M19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z\"></path></svg></a>");

	    	unitData.add(unit.getName());
	    		    	
	    	int count = tableService.countInUnit(unit);
	    	unitData.add(String.valueOf(count));
	    	unitData.add(unit.getBemerkung());	

	    	
	    	unitsList.add(unitData);
	    }
		return unitsList;
	}
	
	public List<List<String>> listTableBooking() {
		List<Unit> units = unitRepository.findAllByOrderByNameAsc();
	    List<List<String>> unitsList = new LinkedList<List<String>>();
	    
	    /*    Comparator<Unit> comparator = new Comparator<Unit>() {
		    @Override
		    public int compare(Unit left, Unit right) {
		        return (int) (left.getId() - right.getId());
		    }
		};
		Collections.sort(units, comparator);*/
	    
	    for (Unit unit:units) {	 
	    	List<String> unitData = new ArrayList<String>();

			unitData.add("<a style=\"color: #F900FF\" href=\"/book/unit/" + unit.getId() + "\"><svg fill=\"#000000\" height=\"24\" viewBox=\"0 0 24 24\" width=\"24\" xmlns=\"http://www.w3.org/2000/svg\"><path d=\"M8.59 16.34l4.58-4.59-4.58-4.59L10 5.75l6 6-6 6z\"/><path d=\"M0-.25h24v24H0z\" fill=\"none\"/></svg></a>");
			unitData.add(unit.getName());
			unitData.add(unit.getBemerkung());
			unitsList.add(unitData);

	    }
		return unitsList;
	}
	
	public Map<String, Object> listExport() {
		List<Unit> units = unitRepository.findAllByOrderByNameAsc();
		/*
	    Comparator<Unit> comparator = new Comparator<Unit>() {
		    @Override
		    public int compare(Unit left, Unit right) {
		        return (int) (left.getId() - right.getId());
		    }
		};
		Collections.sort(units, comparator);*/
		
		Map<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
		for (Unit unit : units) {	
			Map<String, Object> linkedHashMapMap = new LinkedHashMap<String, Object>();
			linkedHashMapMap.put("id", unit.getId());
			linkedHashMapMap.put("name", unit.getName());
			linkedHashMapMap.put("address", unit.getName());
			linkedHashMapMap.put("bemerkung", unit.getBemerkung());
			linkedHashMapMap.put("roomsCount", tableService.countInUnit(unit));
			linkedHashMap.put("unit " + String.valueOf(unit.getId()), linkedHashMapMap);
		}
		return linkedHashMap;
	}

}
