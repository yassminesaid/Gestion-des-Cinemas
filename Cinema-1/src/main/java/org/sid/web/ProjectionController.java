package org.sid.web;

import java.sql.Date;
import java.time.LocalDate;

import org.sid.dao.Film;
import org.sid.dao.Projection;
import org.sid.dao.Salle;
import org.sid.dao.Seance;
import org.sid.dao.Ticket;
import org.sid.repositories.FilmRepository;
import org.sid.repositories.ProjectionRepository;
import org.sid.repositories.SeanceRepository;
import org.sid.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/config")
public class ProjectionController {

	@Autowired private ProjectionRepository projectionRepository;
	@Autowired private FilmRepository filmRepository;
	@Autowired private SeanceRepository seanceRepository;
	@Autowired private TicketRepository ticketRepository;
	
	@GetMapping(path = "/projections")
	public String projections(Model model,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "date", required = false) Date date,
			@RequestParam(name = "salle") Salle salle) {
		if(date == null) date = Date.valueOf(LocalDate.now());
		Page<Projection> pageProjections = projectionRepository
				.findByDateAndSalle(salle.getId(), date, PageRequest.of(page, size));
		
		model.addAttribute("pageProjections", pageProjections);
		model.addAttribute("currentPage", page);
		model.addAttribute("size", size);
		model.addAttribute("date", date);
		model.addAttribute("salle", salle);
		model.addAttribute("pages", new int[pageProjections.getTotalPages()]);
		return "projectionVues/projections";
	}
	
	@GetMapping(path = "/deleteProjection")
	public String deleteProjection(Long id, 
			@RequestParam(name = "date") Date date,
			@RequestParam(name = "salle") Salle salle,
			int page, int size) {
		projectionRepository.deleteById(id);
		ticketRepository.deleteTicketsByProjection(id);
		return "redirect:/config/projections?page="+page+"&size="+size+"&date="+date
				+"&salle="+salle.getId();
	}
	
	@GetMapping(path = "/addProjection")
	public String ajouteProjection(Model model,
			@RequestParam(name = "salle") Salle salle, 
			@RequestParam(name = "error_date", required = false) String error_date,
			@RequestParam(name = "error_seance", required = false) String error_seance) {
		model.addAttribute("projection", new Projection());
		model.addAttribute("salle", salle);
		model.addAttribute("films", filmRepository.findAll());
		model.addAttribute("seances", seanceRepository.findAll());
		model.addAttribute("error_date", error_date);
		model.addAttribute("error_seance", error_seance);
		return "projectionVues/addProjection";
	}
	
	@GetMapping(path = "/editProjection")
	public String editProjection(Model model, Long id,
			@RequestParam(name = "salle") Salle salle) {
		Projection projection = projectionRepository.findById(id).get();
		model.addAttribute("projection", projection);
		model.addAttribute("salle", salle);
		model.addAttribute("films", filmRepository.findAll());
		model.addAttribute("seances", seanceRepository.findAll());
		return "projectionVues/editProjection";
	}
	
	@PostMapping(path = "/saveProjection")
	public String save(Model model, Projection projection, 
			@RequestParam("dateProjection") Date date,
			@RequestParam(name = "salle") Salle salle,
			@RequestParam(name = "film") Film film,
			@RequestParam(name = "seance") Seance seance) {
		
		if(date.compareTo(Date.valueOf(LocalDate.now())) < 0) {
			return "redirect:/config/addProjection?salle="+salle.getId()+
					"&error_date=Date de projection doit etre dans le future";
		}
		if(projectionRepository.findByDateAndSalleAndSeance(salle.getId()
				, date, seance.getId()) != null) {
			return "redirect:/config/addProjection?salle="+salle.getId()+
					"&error_seance=Cette seance est deja utilise";
		}
		projection.setDateProjection(date);
		projection.setSalle(salle);
		projection.setFilm(film);
		projection.setSeance(seance);
		projectionRepository.save(projection);
		salle.getPlaces().forEach(place -> {
			Ticket ticket = new Ticket();
			ticket.setPlace(place);
			ticket.setPrix(projection.getPrix());
			ticket.setProjection(projection);
			ticket.setReserve(false);
			ticketRepository.save(ticket);
		});
		return "redirect:/config/projections?salle="+salle.getId();
	}
	
}
