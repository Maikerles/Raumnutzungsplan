package com.sae.fds.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.sae.fds.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sae.fds.domain.Account;
import com.sae.fds.repository.AccountRepository;

@Controller
public class AccountController {
	private Logger log = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	protected AuthenticationManager authenticationManager;
	
	@Autowired
	private AccountService accountService;
	

	@RequestMapping("/welcome")
	public String login(Account account) {

	   return "welcome";
	}
	
	@RequestMapping("/")
	public String home(Map<String, Object> model) {
		model.put("date", new Date());
		model.put("user", accountService.getLoggedInAccount());
		return "home";
	}
	
	@RequestMapping("/account/list")
	public String list(ModelMap map) {
	   map.addAttribute("accounts", accountService.listTable());
	   return "account/list";
	}
	
	@RequestMapping("/account/list/group")
	public String listGroup(ModelMap map) {
	   map.addAttribute("accounts", accountService.listTableGroup());
	   return "account/listgroup";
	}
	
	@ResponseBody
	@RequestMapping(value ="/account/list/export")
	public String listDownload(HttpServletResponse response) {
		
		String fileName = "userList.json";
		response.setContentType("application/force-download");
		response.setHeader("Content-Disposition", "attachment; filename="+fileName);
		try {
			response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String content = gson.toJson(accountService.listExport());
		return content;
	}

	@GetMapping("/forgotpassword")
	public String forgotPassword(){
		return "forgotpassword";
	} 
	
	@RequestMapping(value = "/account/register", method = RequestMethod.GET)
	public String register(Account account, Model model) {
	   return "account/register";
	}
	
	@RequestMapping(value = "/account/register", method = RequestMethod.POST)
	public String registerPost(@Valid Account account, BindingResult result, Model model) {

	   if (result.hasErrors()) {
	       return "account/register";
	   }
	   
	   Account registeredAccount = accountService.register(account);
	   if (registeredAccount != null) {
	       return "redirect:/account/list";
	   } else {
	       log.error("Account already exists: " + account.getUserName());
	       result.rejectValue("email", "error.alreadyExists", "This username or email already exists, please try to reset password instead.");
	       return "account/register";
	   }
	}
	
	@RequestMapping("/account/delete")
	public String delete(Long id) {
		accountService.delete(id);
	   return "redirect:/account/list";
	}
	
	@RequestMapping("/account/autologin")
	public String autoLogin(Account account) {
		accountService.autoLogin(account.getUserName());
	   return "redirect:/";
	}
	
	@RequestMapping("/account/edit/{id}")
	public String edit(@PathVariable("id") Long id, Account account, Model model) {
	   Account a;
	   Account loggedInUser = accountService.getLoggedInAccount();
	   if(id == 0) {
	       id = loggedInUser.getId();
	   }
	   if(loggedInUser.getId() != id && !loggedInUser.isAdmin()) {
	       return "account/premission-denied";
	   } else if (loggedInUser.isAdmin()) {
	       a = accountRepository.getOne(id);
	   } else {
	       a = loggedInUser;
	   }
	   account.setId(a.getId());
	   account.setUserName(a.getUserName());
	   account.setCompanyName(a.getCompanyName());
	   account.setEmail(a.getEmail());
	   account.setFirstName(a.getFirstName());
	   account.setLastName(a.getLastName());
	   account.setRole(a.getRole());

	
	   return "account/edit";
	}
	
	@RequestMapping(value = "/account/edit", method = RequestMethod.POST)
	public String editPost(@Valid Account account, BindingResult result) {
		 
	   if (result.hasFieldErrors("email")) {
	       return "account/edit";
	   }
	   
	   if(accountService.getLoggedInAccount().isAdmin()) {
		   accountService.updateAccount(account);
	   } else {
		   accountService.updateAccount(accountService.getLoggedInAccount().getUserName(), account);
	   }
	
	   if (accountService.getLoggedInAccount().getId().equals(account.getId())) {
	       // put updated user to session
		   accountService.getLoggedInAccount(true);
	   }
	
	   return "redirect:/account/edit/" + account.getId() + "?updated";
	}
	
}
