package com.sae.fds.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sae.fds.domain.InfoVersion;
import com.sae.fds.repository.InfoVersionRepository;

/* Bearbeitung der Daten */

@Service
public class InfoVersionService {

	@Autowired
	private InfoVersionRepository infoVersionRepository;

	public InfoVersion getCurrent() {
		var info = infoVersionRepository.findAll();

		return info.get(0);
	}

	public InfoVersion findById(Long id) {
		InfoVersion info = infoVersionRepository.findById(id);
		return info;
	}

	public InfoVersion add(InfoVersion info) {
		if (this.infoVersionRepository.findByInfo(info.getInfo()) == null) {
			this.infoVersionRepository.save(info);
			return info;
		} else {
			return null;
		}
	}

	public List<String> listTable() {
		var current = infoVersionRepository.findAll().get(0);

		List<String> infoData = new ArrayList<String>();

		infoData.add("<a style=\"color: #f9b012\" href=\"/info/edit/" + current.getId()
				+ "\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\"><path d=\"M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25z\"></path><path d=\"M20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z\"></path></svg></a>");
		infoData.add(current.getInfo());

		return infoData;

	}

	public InfoVersion update(InfoVersion info) {
		return update(info.getId(), info);
	}

	public InfoVersion update(Long id, InfoVersion newData) {

		String checkInfo = newData.getInfo();
		Long checkId = id;

		InfoVersion check = this.infoVersionRepository.findByInfo(newData.getInfo());
		if (check != null) {
			checkInfo = check.getInfo();
			checkId = check.getId();
		}

		if (checkInfo.equals(newData.getInfo()) || checkId.equals(id)) {
			this.infoVersionRepository.updateInfoVersion(id, newData.getInfo());
			return newData;
		} else {
			return null;
		}

	}

	public Map<String, Object> listExport() {
		List<InfoVersion> infos = infoVersionRepository.findAll();
		/*
		 * Comparator<Unit> comparator = new Comparator<Unit>() {
		 * 
		 * @Override public int compare(Unit left, Unit right) { return (int)
		 * (left.getId() - right.getId()); } }; Collections.sort(units, comparator);
		 */

		Map<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
		for (InfoVersion info : infos) {
			Map<String, Object> linkedHashMapMap = new LinkedHashMap<String, Object>();
			linkedHashMapMap.put("id", info.getId());
			linkedHashMapMap.put("info", info.getInfo());
			linkedHashMap.put("infoVersion " + String.valueOf(info.getId()), linkedHashMapMap);
		}
		return linkedHashMap;
	}

}
