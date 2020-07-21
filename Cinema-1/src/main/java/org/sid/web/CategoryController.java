package org.sid.web;

import org.sid.dao.Categorie;
import org.sid.repositories.CategoryRepository;
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
public class CategoryController {
	
	@Autowired private CategoryRepository categoryRepository;
	
	@GetMapping(path = "/categories")
	public String Categories(Model model,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "keyWord", defaultValue = "") String keyWord) {
		Page<Categorie> pageCategories = categoryRepository
				.findByNameContains(keyWord, PageRequest.of(page, size));
		model.addAttribute("pageCategories", pageCategories);
		model.addAttribute("currentPage", page);
		model.addAttribute("size", size);
		model.addAttribute("keyWord", keyWord);
		model.addAttribute("pages", new int[pageCategories.getTotalPages()]);
		return "filmCategoryVues/categories";
	}
	
	@GetMapping(path = "/deleteCategory")
	public String deleteCategory(Long id, String keyWord, int page, int size) {
		categoryRepository.deleteById(id);
		return "redirect:/config/categories?page="+page+"&size="+size+"&keyWord="+keyWord;
	}
	
	@GetMapping(path = "/addCategory")
	public String ajouteCategory(Model model) {
		model.addAttribute("category", new Categorie());
		return "filmCategoryVues/addCategory";
	}
	
	@GetMapping(path = "/editCategory")
	public String editCategory(Model model, Long id) {
		Categorie category = categoryRepository.findById(id).get();
		model.addAttribute("category", category);
		return "filmCategoryVues/editCategory";
	}
	
	@PostMapping(path = "/saveCategory")
	public String save(Model model, Categorie category) {
		categoryRepository.save(category);
		return "redirect:/config/categories";
	}

}
