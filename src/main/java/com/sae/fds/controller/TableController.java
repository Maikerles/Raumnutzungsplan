package com.sae.fds.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sae.fds.domain.Table;
import com.sae.fds.domain.Unit;
import com.sae.fds.service.TableService;
import com.sae.fds.service.UnitService;

/* Hier werden dei Seiten definiert */

@Controller
public class TableController {
	
	private Logger log = LoggerFactory.getLogger(TableController.class);
	
	@Autowired
	private TableService tableService;
	
	@Autowired
	private UnitService unitService;
	
	@RequestMapping("/room/list")
	public String list(ModelMap map) {
	   map.addAttribute("table", tableService.listTable());
	   return "room/list";
	}
	
	@ResponseBody
	@RequestMapping(value ="/room/list/export")
	public String listDownload(HttpServletResponse response) {
		String fileName = "roomList.json";
		response.setContentType("application/force-download");
		response.setHeader("Content-Disposition", "attachment; filename="+fileName);
		try {
			response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String content = gson.toJson(tableService.listExport());
		return content;
	}
	
	@RequestMapping(value = "/room/add", method = RequestMethod.GET)
	public String add(Table table, Model model) {

		List<Unit> unitList = unitService.list();
		model.addAttribute("unitList", unitList);
	   return "room/add";
	}
	
	@RequestMapping(value = "/room/add", method = RequestMethod.POST)
	public String addPost(@Valid Table table, BindingResult result, Model model) {
	

		List<Unit> unitList = unitService.list();
		model.addAttribute("unitList", unitList);

	   if (result.hasErrors()) {
	       return "room/add";
	   }

	   Table registeredTable = tableService.add(table);
	   if (registeredTable != null) {
	      return "redirect:/room/list";
	   } else {
	       log.error("Table already exists: " + table.getName());
	       return "redirect:/room/add" + "?error";
	   }
	}
	
	@RequestMapping("/room/edit/{id}")
	public String edit(@PathVariable("id") Long id, Table table, Model model) {
		Table r = tableService.findById(id);
		table.setId(r.getId());
		table.setName(r.getName());
		table.setUnit(r.getUnit());
		table.setPosition(r.getPosition());
		List<Unit> unitList = unitService.list();
		model.addAttribute("unitList", unitList);
	   
	   return "room/edit";
	}

	@RequestMapping(value = "/room/edit", method = RequestMethod.POST)
	public String editPost(@Valid Table table, BindingResult result, Model model) {
		 
		List<Unit> unitList = unitService.list();
		model.addAttribute("unitList", unitList);
		
	   if (result.hasFieldErrors("name") || result.hasFieldErrors("position") || result.hasFieldErrors("unit")) {
	       return "room/edit";
	   }
	   if(!table.getName().matches("[0-9].[0-9]+-[0-9]+"))
	   {
		   result.rejectValue("name","error.namedoesnotmatchconvention" ,"Der angegebene Arbeitsplatzname enspricht nicht der Konvention");
		   return "room/edit";
	   }

		Table roo = tableService.update(table);
		
		if (roo == null) {
			return "redirect:/room/edit/" + table.getId() + "?error";
		} else {
			return "redirect:/room/edit/" + table.getId() + "?updated";
		}
	}
	
	@RequestMapping("/room/delete")
	public String delete(Long id, ModelMap modelMap) {
		tableService.delete(id);
			modelMap.addAttribute("table", tableService.listTable());
		   return "room/list";		
	}
	

}
