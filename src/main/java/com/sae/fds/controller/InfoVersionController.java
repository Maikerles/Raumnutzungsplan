package com.sae.fds.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sae.fds.domain.InfoVersion;
import com.sae.fds.service.InfoVersionService;

/* Hier werden dei Seiten definiert */

@Controller
public class InfoVersionController {
	
	@Autowired
	private InfoVersionService infoVersionService;
	

	@RequestMapping("/info/infoV")
	public String list(ModelMap map) {
	   map.addAttribute("info", infoVersionService.listTable());
	   return "info/infoV";
	}
	@RequestMapping("/info/edit/{id}")
	public String edit(@PathVariable("id") Long id, InfoVersion info, Model model) {
		InfoVersion u = infoVersionService.findById(id);
		info.setId(u.getId());
		info.setInfo(info.getInfo());
	   return "info/edit";
	}
	@RequestMapping(value = "/info/edit", method = RequestMethod.POST)
	public String editPost(@Valid InfoVersion info, BindingResult result, Model model) {
	
		if (result.hasFieldErrors("info")) {
		       return "info/edit";
		   }
		 
			InfoVersion infoV = infoVersionService.update(info);
			
			if (infoV == null) {
				return "redirect:/info/edit/" + info.getId() + "?error";
			} else {
				return "redirect:/info/edit/" + info.getId() + "?updated";
			}
	}

	
}
