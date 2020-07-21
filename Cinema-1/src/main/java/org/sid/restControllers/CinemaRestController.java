package org.sid.restControllers;

import java.util.Collection;

import org.sid.dao.Salle;
import org.sid.repositories.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CinemaRestController {

	@Autowired
	private CinemaRepository cinemaRepository;
	
	@GetMapping("/client/cinemas/{id}/salles")
	public Collection<Salle> villes(@PathVariable Long id) {
		return cinemaRepository.findById(id).get().getSalles();
	}
	
	
}
