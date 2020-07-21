package org.sid.dao;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
 
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor 
public class Projection {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private java.sql.Date dateProjection;
	private double prix;
	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	private Salle salle;
	@ManyToOne
	private Film film;
	@OneToMany(mappedBy = "projection")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Collection<Ticket> tickets;
	@ManyToOne
	private Seance seance;
	
	public String toString() {
		return "{ id : " + id + ", dateProjection : " + dateProjection + ", prix : "
					+ prix + ", salle_id : " + salle.getId() + ", film_titre : " + 
					film.getTitre() + ", seance : " + seance.getHeureDebut();
	}
	
}
