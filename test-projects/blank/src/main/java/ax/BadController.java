package ax;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Arne Vandamme
 * @since 1.0.0
 */
@Controller
public class BadController
{
	@RequestMapping("/bad")
	public String bad() {
		return "bad";
	}
}
