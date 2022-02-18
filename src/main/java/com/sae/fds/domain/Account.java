package com.sae.fds.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*Hier Datenbanktabellen generiert*/

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Account {
    @GeneratedValue
    @Id
    private Long id;
    
    @NotNull
    @Size(min = 1, max = 100, message = " Benutzername max. 100 Zeichen")
    // equivalent to  
    private String userName;
    
    @NotNull
    @Size(min = 8, max = 100, message = "mind. 8 Zeichen für das Passwort")
    private String password;
    
    @Transient
    private String confirmPassword;
    
    @Email(message = "Emailadresse ist nicht valide.")
    @NotNull
    private String email;
    
    private String token;
    
    private String role = "ROLE_USER";
    
    @Size(min = 1, max = 100, message = "Vorname max. 100 Zeichen")
    private String firstName;
    @Size(min = 1, max = 100, message = "Nachname max. 100 Zeichen")
    private String lastName;
    @Size(min = 1, max = 100, message = "Projektleiter max. 100 Zeichen")
    private String projectSupervisor;
    @Size(min = 1, max = 100, message = "Projekt max. 100 Zeichen")
    private String projectName;
    
    private String lastLogin;

    
    public Boolean isAdmin() {
        return this.role.equals("ROLE_ADMIN");
    }
    
    public Boolean isMatchingPasswords() {
        return this.password.equals(this.confirmPassword);
    }

}
