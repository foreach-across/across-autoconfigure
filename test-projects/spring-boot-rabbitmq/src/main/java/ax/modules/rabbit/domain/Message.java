package ax.modules.rabbit.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Steven Gentens
 */
@Getter
@Service
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Message
{
	private MessageType type;
	private String content;
}
