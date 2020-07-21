package org.sid.restControllers;

import org.sid.dao.Ticket;
import org.sid.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class TicketRestController {

	@Autowired private TicketRepository ticketRepository;
	
	@GetMapping("/client/tickets/{id}")
	public Ticket getTicket(@PathVariable Long id) {
		return ticketRepository.findById(id).get();
	}
	
	@PutMapping("/client/tickets/{id}")
	public Ticket update(@RequestBody Ticket ticket, @PathVariable Long id) {
		ticket.setId(id);
		return ticketRepository.save(ticket);
	}

}
