package com.sae.fds.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.sae.fds.service.TableService;
import com.sae.fds.service.UnitService;
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
import com.sae.fds.domain.Unit;

@Controller
public class UnitController {
	
	private Logger log = LoggerFactory.getLogger(UnitController.class);
	
	@Autowired
	private UnitService unitService;
	
	@Autowired
	private TableService tableService;

	@RequestMapping("/unit/list")
	public String list(ModelMap map) {
	   map.addAttribute("units", unitService.listTable());
	   return "unit/list";
	}
	
	@RequestMapping(value ="/unit/list/export")
	public @ResponseBody String listDownload(HttpServletResponse response) {
		String fileName = "unitList.json";
		response.setContentType("application/force-download");
	  response.setHeader("Content-Disposition", "attachment; filename="+fileName);
	  try {
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	  Gson gson = new GsonBuilder().setPrettyPrinting().create();
	  String content = gson.toJson(unitService.listExport());
	  return content;
	}
	
	@RequestMapping(value = "/unit/add", method = RequestMethod.GET)
	public String add(Unit unit, Model model) {

	   return "unit/add";
	}
	
	@RequestMapping(value = "/unit/add", method = RequestMethod.POST)
	public String addPost(@Valid Unit unit, BindingResult result, Model model) {

	   if (result.hasErrors()) {
	       return "unit/add";
	   }
	   
	   Unit registeredUnit = unitService.add(unit);
	   if (registeredUnit != null) {
	      return "redirect:/unit/list";
	   } else {
	       log.error("Unit already exists: " + unit.getName());
	       //result.rejectValue("organization", "error.alreadyExists", "This organization already exists, please try another name.");
	       return "redirect:/unit/add" + "?error";
	   }
	}
	
	@RequestMapping("/unit/edit/{id}")
	public String edit(@PathVariable("id") Long id, Unit unit, Model model) {
		Unit u = unitService.findById(id);
		unit.setId(u.getId());
		unit.setName(u.getName());

	   
	   return "unit/edit";
	}
	
	@RequestMapping(value = "/unit/edit", method = RequestMethod.POST)
	public String editPost(@Valid Unit unit, BindingResult result, Model model) {
		

		 
	   if (result.hasFieldErrors("name")) {
	       return "unit/edit";
	   }

		Unit uni = unitService.update(unit);
		
		if (uni == null) {
			return "redirect:/unit/edit/" + unit.getId() + "?error";
		} else {
			return "redirect:/unit/edit/" + unit.getId() + "?updated";
		}
	}
	
	@RequestMapping("/unit/delete")
	public String delete(Long id) {
		var unit = unitService.findById(id);
		var tables = tableService.findByUnit(unit);
		
		for (var table : tables){
			tableService.delete(table.getId());
		}

		unitService.delete(id);
		return "redirect:/unit/list";
	}
	
}
