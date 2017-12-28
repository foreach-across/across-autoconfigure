package ax.application.web;

import ax.application.Events;
import ax.application.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Steven Gentens
 */
@Controller
@RequiredArgsConstructor
public class HelloController
{
	private final MessageSender sender;

	@GetMapping("/greet/{name}")
	public ResponseEntity greeting( @PathVariable String name ) {
		sender.sendMessage( Events.HELLO, name );
		return ResponseEntity.ok( name );
	}

	@GetMapping("/farewell/{name}")
	public ResponseEntity farewell( @PathVariable String name ) {
		sender.sendMessage( Events.FAREWELL, name );
		return ResponseEntity.ok( name );
	}
}
