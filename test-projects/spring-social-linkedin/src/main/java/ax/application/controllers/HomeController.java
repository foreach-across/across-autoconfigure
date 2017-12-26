package ax.application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Steven Gentens
 */
@Controller
public class HomeController
{
	@RequestMapping("/")
	public String homepage() {
		return "th/springBootSocialLinkedIn/homepage";
	}
}
