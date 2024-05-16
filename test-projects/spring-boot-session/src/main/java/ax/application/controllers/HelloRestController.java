package ax.application.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
public class HelloRestController
{

	@GetMapping("/")
	String uid( HttpSession session ) {
		session.setAttribute("user", session.getId());
		return session.getId();
	}

}
