package ax.application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Steven Gentens
 */
@Controller
public class HomeController
{
	@GetMapping("/")
	public String homeController(){
		return "th/springBootWebsocket/index";
	}
}
