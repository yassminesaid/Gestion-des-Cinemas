package org.sid.restControllers;

import java.sql.Date;
import java.util.Collection;

import org.sid.dao.Projection;
import org.sid.dao.Ticket;
import org.sid.repositories.ProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectionsRestController {

	@Autowired
	private ProjectionRepository projectionRepository;
	
	@GetMapping("/client/projections/{id}/tickets")
	public Collection<Ticket> tickets(@PathVariable Long id) {
		return projectionRepository.findById(id).get().getTickets();
	}
	
	@GetMapping("/client/salles/{id}/projections/{date}")
	public Collection<Projection> projections(@PathVariable Long id,
			@PathVariable Date date) {
		return projectionRepository.findProjectionBySalleAndDate(id, date);
	}
	
}
