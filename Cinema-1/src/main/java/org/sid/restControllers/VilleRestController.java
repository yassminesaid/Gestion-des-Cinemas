package org.sid.restControllers;

import java.util.List;

import org.sid.dao.Ville;
import org.sid.repositories.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class VilleRestController {

	@Autowired
	private VilleRepository villeRepository;
	
	@GetMapping("/client/villes")
	public List<Ville> villes() {
		return villeRepository.findAll();
	}
	
}
