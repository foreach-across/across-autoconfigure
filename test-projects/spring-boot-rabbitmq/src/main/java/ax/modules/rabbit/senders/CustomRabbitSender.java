package ax.modules.rabbit.senders;

import ax.modules.rabbit.config.RabbitConfiguration;
import ax.modules.rabbit.domain.Message;
import ax.modules.rabbit.domain.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Steven Gentens
 */
@Service
@RequiredArgsConstructor
public class CustomRabbitSender
{
	private final RabbitTemplate rabbitTemplate;

	public void send( MessageType type, String content ) {
		rabbitTemplate.convertAndSend( RabbitConfiguration.EXCHANGE_NAME,
		                               RabbitConfiguration.ROUTING_KEY,
		                               Message.builder()
		                                      .type( type )
		                                      .content( content )
		                                      .build()
		);
	}
}
