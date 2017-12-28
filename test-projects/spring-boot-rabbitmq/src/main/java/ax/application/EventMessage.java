package ax.application;

import lombok.*;

import java.io.Serializable;

/**
 * @author Steven Gentens
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class EventMessage implements Serializable
{
	private Events event;
	private String content;

	@Override
	public String toString() {
		return "EventMessage{" +
				"event=" + event +
				", content='" + content + '\'' +
				'}';
	}
}
