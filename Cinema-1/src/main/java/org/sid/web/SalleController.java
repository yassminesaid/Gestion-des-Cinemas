package org.sid.web;

import java.util.ArrayList;
import java.util.List;

import org.sid.DeleteController;
import org.sid.dao.Cinema;
import org.sid.dao.Place;
import org.sid.dao.Salle;
import org.sid.repositories.CinemaRepository;
import org.sid.repositories.PlaceRepository;
import org.sid.repositories.SalleRepository;
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
public class SalleController {
	
	@Autowired private SalleRepository salleRepository;
	@Autowired private PlaceRepository placeRepository;
	@Autowired private CinemaRepository cinemaRepository;
	@Autowired private DeleteController deleteController;

	@GetMapping(path = "/salles")
	public String salles(Model model,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "cinema") Cinema cinema,
			@RequestParam(name = "keyWord", defaultValue = "") String keyWord) {
		if(cinema == null) cinema = cinemaRepository.findAll().get(0);
		Page<Salle> pageSalles = salleRepository
				.findByCinema(cinema.getId(), "%"+keyWord+"%", PageRequest.of(page, size));
		model.addAttribute("pageSalles", pageSalles);
		model.addAttribute("currentPage", page);
		model.addAttribute("size", size);
		model.addAttribute("cinema", cinema);
		model.addAttribute("keyWord", keyWord);
		model.addAttribute("pages", new int[pageSalles.getTotalPages()]);
		return "salleVues/salles";
	}
	
	@GetMapping(path = "/deleteSalle")
	public String deleteSalle(Long id,
			@RequestParam(name = "cinema") Cinema cinema,
			String keyWord, int page, int size) {
		deleteController.deleteSalle(id);
		return "redirect:/config/salles?page="+page+"&size="+size+"&cinema="
				+cinema.getId()+"&keyWord="+keyWord;
	}
	
	@GetMapping(path = "/addSalle")
	public String ajouteSalle(Model model,
			@RequestParam(name = "cinema_id") Long cinema_id) {
		model.addAttribute("salle", new Salle());
		model.addAttribute("cinema", cinemaRepository.findById(cinema_id).get());
		return "salleVues/addSalle";
	}
	
	@GetMapping(path = "/editSalle")
	public String editSalle(Model model, Long id,
			@RequestParam(name = "cinema") Cinema cinema) {
		Salle salle = salleRepository.findById(id).get();
		model.addAttribute("salle", salle);
		model.addAttribute("cinema", cinema);
		return "salleVues/editSalle";
	}
	
	@PostMapping(path = "/updateSalle")
	public String updateSalle(Model model, Salle salle,
			@RequestParam(name = "cinema") Cinema cinema) {
		int newNbrPlaces = salle.getNombrePlace();
		Salle s = salleRepository.findById(salle.getId()).get();
		int oldNbrPlaces = s.getNombrePlace();
		if(newNbrPlaces > oldNbrPlaces) {
			insertPlaces(s, newNbrPlaces - oldNbrPlaces);
		}
		else if(newNbrPlaces < oldNbrPlaces) {
			int x = oldNbrPlaces - newNbrPlaces;
			List<Place> places = new ArrayList<Place>(s.getPlaces());
			for(int i = 0; i < x; i++) {
				Place place = places.get(places.size() - 1);
				placeRepository.deleteById(place.getId());
				places.remove(place);
				s.getPlaces().remove(place);
			}	
		}
		salleRepository.save(salle);
		return "redirect:/config/salles?cinema="+cinema.getId();
	}
	
	public void insertPlaces(Salle salle, int nbr) {
		int index = salle.getPlaces() != null && salle.getPlaces().size() > 0 ?
				salle.getPlaces().size() : 0;
		for(int i = 0; i < nbr; i++) {
			Place place = new Place();
			place.setNumero(index+i+1);
			place.setSalle(salle);
			placeRepository.save(place);
		}
	}
	
	@PostMapping(path = "/saveSalle")
	public String save(Model model, Salle salle, 
			@RequestParam(name = "cinema") Cinema cinema) {
		salle.setCinema(cinema);
		salleRepository.save(salle);
		insertPlaces(salle, salle.getNombrePlace());
		return "redirect:/config/salles?cinema="+cinema.getId();
	}
	
	
}
