package org.sid.dao;

import java.io.Serializable;
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
public class Cinema implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private double longitude, latitude, altitude;
	private int nombreSalles;
	@OneToMany (mappedBy = "cinema")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Collection<Salle> salles;
	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	private Ville ville;
	
	public String toString() {
		return "{ id : " + id + ", name : " + name + ", nombrePlace : " + nombreSalles
				+ ", longitude : " + longitude + ", latitude : " + latitude 
				+ ", altitude : " + altitude
				+ ", salles : " + salles.size() + ", ville id : " + ville.getId() + " }";
	}
	
}
