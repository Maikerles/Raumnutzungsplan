package com.sae.fds.domain;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*Hier Datenbanktabellen generiert*/

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Event {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="start")
	private Date start;
	
	@Column(name="stop")
	private Date end;
	
    @NotNull
    @ManyToOne
    private Table table;
   
    @NotNull
    @ManyToOne
    private Account account;
	
	public Event(Date start, Date end, Table table, Account account) {
		super();
		this.start = start;
		this.end = end;
		this.table = table;
		this.account = account;
	}
	
	@Override
	public String toString() {
		return "Event [id=" + id + ", title=" + ", description="
				+ ", speaker=" + ", start=" + start
				+ ", stop=" + end + ", table=" + table + ", account=" + account + "]";
	}
}