package com.sae.fds.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sae.fds.domain.Account;
import com.sae.fds.repository.AccountRepository;

/* Bearbeitung der Daten */

@Service
public class AccountService implements UserDetailsService {

    @Value("${app.key}")
    private String applicationSecret;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private HttpSession httpSession;

    public final String CURRENT_ACCOUNT_KEY = "CURRENT_ACCOUNT";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findOneByUserNameOrEmail(username, username);

        if (account == null) {
            throw new UsernameNotFoundException(username);
        }

        httpSession.setAttribute(CURRENT_ACCOUNT_KEY, account);
        List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(account.getRole());

        return new org.springframework.security.core.userdetails.User(account.getUserName(), account.getPassword(),
                auth);
    }

    public void autoLogin(Account account) {
        autoLogin(account.getUserName());
    }

    public void autoLogin(String username) {
        UserDetails userDetails = this.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
        if (auth.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }

    public Account register(Account account) {
        account.setPassword(encodeUserPassword(account.getPassword()));

        if (this.accountRepository.findOneByUserName(account.getUserName()) == null
                && this.accountRepository.findOneByEmail(account.getEmail()) == null) {
            String activation = createActivationToken(account, false);
            account.setToken(activation);
            this.accountRepository.save(account);
            return account;
        }

        return null;
    }

    public String encodeUserPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public Boolean delete(Long id) {
        this.accountRepository.deleteById(id);;
        return true;
    }

    public Account activate(String activation) {
        if (activation.equals("1") || activation.length() < 5) {
            return null;
        }
        Account a = this.accountRepository.findOneByToken(activation);
        if (a != null) {
            a.setToken("1");
            this.accountRepository.save(a);
            return a;
        }
        return null;
    }

    public String createActivationToken(Account account, Boolean save) {
        PasswordEncoder encoder = PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
        String activationToken = encoder.encode(account.getUserName()); 

        if (save) {
            account.setToken(activationToken);
            this.accountRepository.save(account);
        }
        return activationToken;
    }

    public String createResetPasswordToken(Account account, Boolean save) {
        PasswordEncoder encoder = PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
        String resetToken = encoder.encode(account.getEmail()); 
        if(save) {
        	account.setToken(resetToken);
            this.accountRepository.save(account);
        }
        return resetToken;
    }
    
    public Account resetActivation(String email) {
    	Account a = this.accountRepository.findOneByEmail(email);
        if(a != null) {
            createActivationToken(a, true);
            return a;
        }
        return null;
    }
    
    public Boolean resetPassword(Account account) {
    	Account a = this.accountRepository.findOneByUserName(account.getUserName());
        if(a != null) {
            a.setPassword(encodeUserPassword(account.getPassword()));
            a.setToken("1");
            this.accountRepository.save(a);
            return true;
        }
        return false;
    }
    
    public void updateAccount(Account account) {
        updateAccount(account.getUserName(), account);
    }
    
    public void updateAccount(String userName, Account newData) {
    	if(!newData.getPassword().equals("")) {
    		newData.setPassword(encodeUserPassword(newData.getPassword()));
            this.accountRepository.updateUserWithPassword(
                    userName, 
                    newData.getEmail(), 
                    newData.getFirstName(), 
                    newData.getLastName(), 
                    newData.getProjectName(),
                    newData.getPassword(),
                    newData.getRole(), 
                    newData.getProjectSupervisor());
    	}else{
    		this.accountRepository.updateUser(
                    userName, 
                    newData.getEmail(), 
                    newData.getFirstName(), 
                    newData.getLastName(), 
                    newData.getProjectName(),
                    newData.getRole(),
                    newData.getProjectSupervisor());
    	}
    }
    
    public Account getLoggedInAccount() {
        return getLoggedInAccount(false);
    }
    
    public Account getLoggedInAccount(Boolean forceFresh) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = (Account) httpSession.getAttribute(CURRENT_ACCOUNT_KEY);
        if(forceFresh || httpSession.getAttribute(CURRENT_ACCOUNT_KEY) == null) {
        	account = this.accountRepository.findOneByUserName(userName);
            httpSession.setAttribute(CURRENT_ACCOUNT_KEY, account);
        }
        return account;
    }
    
    public void updateLastLogin(String userName) {
        this.accountRepository.updateLastLogin(userName);
    }
    
	public List<List<String>> listTable() {
		List<Account> accounts = accountRepository.findAll();
	    List<List<String>> accountsList = new LinkedList<List<String>>();
	    
	    Comparator<Account> comparator = new Comparator<Account>() {
		    @Override
		    public int compare(Account left, Account right) {
		        return (int) (left.getId() - right.getId());
		    }
		};
		Collections.sort(accounts, comparator);
	    
	    for (Account account:accounts) {
	    	List<String> accountData = new ArrayList<String>();
            accountData.add("<a style=\"color: #f9b012\" href=\"/account/edit/" + account.getId() + "\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\"><path d=\"M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25z\"></path><path d=\"M20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z\"></path></svg></a>");
            accountData.add("<a onClick=\"return confirm('Soll der Datensatz wirklich gelöscht werden?');\" style=\"color: #f9b012\" href=\"/account/delete?id=" + account.getId() + "\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\"><path d=\"M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12z\"></path><path d=\"M19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z\"></path></svg></a>");

	    	accountData.add(account.getUserName());
	    	accountData.add(account.getFirstName());
	    	accountData.add(account.getLastName());
	    	accountData.add(account.getLastLogin());
            accountData.add(account.getRole().replace("ROLE_", ""));
            accountData.add(account.getProjectName());
            accountData.add(account.getProjectSupervisor());

	    	int count = eventService.countInAccount(account);
	    	accountData.add(String.valueOf(count));
	    	if (count != 0) {
	    		accountData.add("<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\"><path d=\"M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12z\" fill=\"#E4E4E4\" ></path><path d=\"M19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z\" fill=\"#E4E4E4\"></path></svg>");
	    	}else{
                accountData.add("<a style=\"color: #f9b012\" href=\"/account/edit/" + account.getId() + "\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\"><path d=\"M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25z\"></path><path d=\"M20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z\"></path></svg></a>");
	    		accountData.add("<a onClick=\"return confirm('Soll der Datensatz wirklich gelöscht werden?');\" style=\"color: #f9b012\" href=\"/account/delete?id=" + account.getId() + "\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\"><path d=\"M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12z\"></path><path d=\"M19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z\"></path></svg></a>");
	    	}
	    	accountData.add("<a style=\"color: #f9b012\" href=\"/account/autologin?userName=" + account.getUserName() + "\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\"><path d=\"M16 1H4c-1.1 0-2 .9-2 2v14h2V3h12V1zm3 4H8c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h11c1.1 0 2-.9 2-2V7c0-1.1-.9-2-2-2zm0 16H8V7h11v14z\"></path></svg></a>");
	    	accountsList.add(accountData);
	    }
		return accountsList;
	}
    
	public List<Account> listExport() {
		List<Account> accounts = accountRepository.findAll();
	    Comparator<Account> comparator = new Comparator<Account>() {
		    @Override
		    public int compare(Account left, Account right) {
		        return (int) (left.getId() - right.getId());
		    }
		};
		Collections.sort(accounts, comparator);
		return accounts;
	}
	
	public List<List<String>> listTableGroup() {
		List<Account> accounts = accountRepository.findAll();
	    List<List<String>> accountsList = new LinkedList<List<String>>();
	    
	    Comparator<Account> comparator = new Comparator<Account>() {
		    @Override
		    public int compare(Account left, Account right) {
		        return (int) (left.getId() - right.getId());
		    }
		};
		Collections.sort(accounts, comparator);
	    
	    for (Account account:accounts) {
	    	List<String> accountData = new ArrayList<String>();
	    	accountData.add("<a style=\"color: #f9b012\" href=\"/account/edit/" + account.getId() + "\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\"><path d=\"M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25z\"></path><path d=\"M20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z\"></path></svg></a>");
	    	accountData.add(account.getUserName());
	    	String cred = account.getLastName() + " " + account.getFirstName();
	    	accountData.add(cred.replace("null", ""));
	    	int count = eventService.countInAccount(account);
	    	accountData.add(String.valueOf(count));
	    	if (count != 0) {
	    		accountData.add("<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\"><path d=\"M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12z\" fill=\"#E4E4E4\" ></path><path d=\"M19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z\" fill=\"#E4E4E4\"></path></svg>");
	    	}else{
	    		accountData.add("<a onClick=\"return confirm('Soll der Datensatz wirklich gelöscht werden?');\" style=\"color: #f9b012\" href=\"/account/delete?id=" + account.getId() + "\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\"><path d=\"M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12z\"></path><path d=\"M19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z\"></path></svg></a>");
	    	}
	    	accountData.add("<a style=\"color: #f9b012\" href=\"/account/autologin?userName=" + account.getUserName() + "\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\"><path d=\"M16 1H4c-1.1 0-2 .9-2 2v14h2V3h12V1zm3 4H8c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h11c1.1 0 2-.9 2-2V7c0-1.1-.9-2-2-2zm0 16H8V7h11v14z\"></path></svg></a>");
	    	accountsList.add(accountData);
	    }
		return accountsList;
	}
	

	
	public Account findByName(String name) {
		return this.accountRepository.findOneByUserName(name);
	}
}
