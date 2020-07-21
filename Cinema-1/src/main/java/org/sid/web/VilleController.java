package org.sid.web;

import org.sid.DeleteController;
import org.sid.dao.Ville;
import org.sid.repositories.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = {"/config", "/"})
public class VilleController {

	@Autowired private VilleRepository villeRepository;
	@Autowired private DeleteController deleteController;
	
	@GetMapping(path = {"/", "/config", "/index"})
	public String index() {
		return "redirect:/config/villes";
	}
	
	@GetMapping(path = "/villes")
	public String villes(Model model,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "keyWord", defaultValue = "") String keyWord) {
		Page<Ville> pageVilles = villeRepository
				.findByNameContains(keyWord, PageRequest.of(page, size));
		model.addAttribute("pageVilles", pageVilles);
		model.addAttribute("currentPage", page);
		model.addAttribute("size", size);
		model.addAttribute("keyWord", keyWord);
		model.addAttribute("pages", new int[pageVilles.getTotalPages()]);
		return "villeVues/villes";
	}
	
	@GetMapping(path = "/deleteVille")
	public String deleteVille(Long id, String keyWord, int page, int size) {
		deleteController.deleteVille(id);
		return "redirect:/config/villes?page="+page+"&size="+size+"&keyWord="+keyWord;
	}
	
	@GetMapping(path = "/addVille")
	public String ajouteVille(Model model) {
		model.addAttribute("ville", new Ville());
		return "villeVues/ajouteVille";
	}
	
	@GetMapping(path = "/editVille")
	public String editVille(Model model, Long id) {
		Ville ville = villeRepository.findById(id).get();
		model.addAttribute("ville", ville);
		return "villeVues/editVille";
	}
	
	@PostMapping(path = "/saveVille")
	public String save(Model model, @Validated Ville ville,
			BindingResult bindingResult) {
		if(bindingResult.hasErrors())
			return "villeVues/ajouteVille";
		villeRepository.save(ville);
		return "redirect:/config/villes";
	}
	
}
