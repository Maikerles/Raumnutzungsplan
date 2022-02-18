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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "table_entity")
public class Table {
    @GeneratedValue
    @Id
    private Long id;
    
    @NotNull
    @Size(min = 1, max = 10, message = "Arbeitsplatz max. 10 Zeichen")
    private String name;

    @NotNull
    private String position;

    @ManyToOne
    @NotNull
    private Unit unit;

    public String getPosition(){
        return position;
    }
    
}