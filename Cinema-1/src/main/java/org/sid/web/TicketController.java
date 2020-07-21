package org.sid.web;

import org.sid.dao.Projection;
import org.sid.dao.Ticket;
import org.sid.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/config")
public class TicketController {

	@Autowired private TicketRepository ticketRepository;
	
	@GetMapping(path = "/tickets")
	public String projections(Model model,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "projection") Projection projection) {
		Page<Ticket> pageTickets = ticketRepository
				.findByProjection(projection.getId(), PageRequest.of(page, size));
		
		model.addAttribute("pageTickets", pageTickets);
		model.addAttribute("currentPage", page);
		model.addAttribute("size", size);
		model.addAttribute("projection", projection);
		model.addAttribute("pages", new int[pageTickets.getTotalPages()]);
		return "ticketVues/tickets";
	}
	
}
