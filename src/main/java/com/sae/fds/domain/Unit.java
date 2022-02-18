package com.sae.fds.domain;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class Unit {
    @GeneratedValue
    @Id
    private Long id;
    
    @NotNull
    @Size(min = 1, max = 10, message = "Raumnummer Max. 10 Zeichen")
    private String name;
    
    @Size(min = 1, max = 50, message = "Bemerkung Max. 50 Zeichen")
    private String bemerkung;
    
}