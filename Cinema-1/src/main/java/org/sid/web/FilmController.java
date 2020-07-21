package org.sid.web;

import java.sql.Date;
import java.util.List;

import org.sid.dao.Categorie;
import org.sid.dao.Film;
import org.sid.repositories.CategoryRepository;
import org.sid.repositories.FilmRepository;
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
public class FilmController {

	@Autowired private FilmRepository filmRepository;
	@Autowired private CategoryRepository categoryRepository;
	
	@GetMapping(path = "/films")
	public String films(Model model,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "keyWord", defaultValue = "") String keyWord) {
		Page<Film> pageFilms = filmRepository
				.findByTitreContains(keyWord, PageRequest.of(page, size));
		model.addAttribute("pageFilms", pageFilms);
		model.addAttribute("currentPage", page);
		model.addAttribute("size", size);
		model.addAttribute("keyWord", keyWord);
		model.addAttribute("pages", new int[pageFilms.getTotalPages()]);
		return "filmVues/films";
	}
	
	@GetMapping(path = "/deleteFilm")
	public String deleteFilm(Long id, String keyWord, int page, int size) {
		filmRepository.deleteById(id);
		return "redirect:/config/films?page="+page+"&size="+size+"&keyWord="+keyWord;
	}
	
	@GetMapping(path = "/addFilm")
	public String ajouteFilm(Model model) {
		List<Categorie> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);
		model.addAttribute("film", new Film());
		return "filmVues/addFilm";
	}
	
	@GetMapping(path = "/editFilm")
	public String editFilm(Model model, Long id) {
		Film film = filmRepository.findById(id).get();
		List<Categorie> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);
		model.addAttribute("film", film);
		return "filmVues/editFilm";
	}
	
	@PostMapping(path = "/saveFilm")
	public String save(Model model, Film film, 
			@RequestParam("category") Categorie categorie,
			@RequestParam("date") Date date) {
		film.setCategorie(categorie);
		film.setDateSortie(date);
		filmRepository.save(film);
		return "redirect:/config/films";
	}
	
}
