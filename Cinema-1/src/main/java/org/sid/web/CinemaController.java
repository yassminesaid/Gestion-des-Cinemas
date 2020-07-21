package org.sid.web;

import java.util.List;

import org.sid.DeleteController;
import org.sid.dao.Cinema;
import org.sid.dao.Place;
import org.sid.dao.Salle;
import org.sid.dao.Ville;
import org.sid.repositories.CinemaRepository;
import org.sid.repositories.PlaceRepository;
import org.sid.repositories.SalleRepository;
import org.sid.repositories.VilleRepository;
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
public class CinemaController {

	@Autowired private VilleRepository villeRepository;
	@Autowired private CinemaRepository cinemaRepository;
	@Autowired private SalleRepository salleRepository;
	@Autowired private PlaceRepository placeRepository;
	@Autowired private DeleteController deleteController;
	
	@GetMapping(path = "/cinemas")
	public String projections(Model model,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "ville", required = false) Ville ville,
			@RequestParam(name = "keyWord", defaultValue = "") String keyword) {
		List<Ville> villes = villeRepository.findAll();
		if(ville == null) ville = villes.get(0);
		Page<Cinema> pageCinemas = cinemaRepository
				.findByVille(ville.getId(), "%"+keyword+"%", PageRequest.of(page, size));
		
		model.addAttribute("pageCinemas", pageCinemas);
		model.addAttribute("currentPage", page);
		model.addAttribute("size", size);
		model.addAttribute("keyWord", keyword);
		model.addAttribute("ville", ville);
		model.addAttribute("villes", villes);
		model.addAttribute("pages", new int[pageCinemas.getTotalPages()]);
		return "cinemaVues/cinemas";
	}
	
	@GetMapping(path = "/deleteCinema")
	public String deleteCinema(Long id, 
			@RequestParam(name = "ville")Ville ville,
			String keyWord, int page, int size) {
		deleteController.deleteCinema(id);
		return "redirect:/config/cinemas?page="+page+"&size="+size+"&ville="+ville.getId()
				+"&keyWord="+keyWord;
	}
	
	@GetMapping(path = "/addCinema")
	public String ajouteCinema(Model model, 
			@RequestParam(name = "ville_id") Long ville_id) {
		model.addAttribute("cinema", new Cinema());
		model.addAttribute("ville", villeRepository.findById(ville_id).get());
		return "cinemaVues/addCinema";
	}
	
	@GetMapping(path = "/editCinema")
	public String editCinema(Model model, Long id) {
		Cinema cinema = cinemaRepository.findById(id).get();
		model.addAttribute("cinema", cinema);
		model.addAttribute("villes", villeRepository.findAll());
		return "cinemaVues/editCinema";
	}
	
	@PostMapping(path = "/updateCinema")
	public String updateCinema(Model model, Cinema cinema,
			@RequestParam(name = "ville") Ville ville) {
		cinema.setVille(ville);
		cinemaRepository.save(cinema);
		return "redirect:/config/cinemas";
	}
	
	@PostMapping(path = "/saveNewCinema")
	public String save(Model model, Cinema cinema, 
			@RequestParam(name = "ville") Ville ville,
			@RequestParam(name = "nombrePlaces") int nombrePlaces) {
		cinema.setVille(ville);
		cinema = cinemaRepository.save(cinema);
		for(int i = 0; i < cinema.getNombreSalles(); i++) {
			Salle salle = new Salle();
			salle.setName("Salle " + (i + 1));
			salle.setCinema(cinema);
			salle.setNombrePlace(nombrePlaces);
			salle = salleRepository.save(salle);
			for(int j = 0; j < salle.getNombrePlace(); j++) {
				Place place = new Place();
				place.setNumero(j+1);
				place.setSalle(salle);
				placeRepository.save(place);
			}
		}
		return "redirect:/config/cinemas";
	}
	
}
