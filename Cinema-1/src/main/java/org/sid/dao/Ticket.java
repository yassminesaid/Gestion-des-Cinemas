package org.sid.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Ticket {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String nomClient;
	private double prix;
	//@Column(unique = true)
	private int codePayement;
	private boolean reserve;
	@ManyToOne
	private Place place;
	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	private Projection projection;
	
	public String toString() {
		return "{ id : " + id + ", nomClient : " + nomClient + ", prix : " + prix  
					+ ", codePayement : " + codePayement + ", reserve : " + reserve
					+ ", place : " + place.getId() + ", projection : " 
					+ projection.getId() ;
	}
	
}
