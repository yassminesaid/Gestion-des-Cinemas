package org.sid.dao;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Seance {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	//@Temporal(TemporalType.TIMESTAMP)
	private java.sql.Time heureDebut;
	
}
