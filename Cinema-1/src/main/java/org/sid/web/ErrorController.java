package org.sid.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

	@GetMapping(path = "/403")
	public String accessDenied() {
		return "403";
	}
	
	@GetMapping(path = "/404")
	public String notFound() {
		return "404";
	}
	
}
