package ax.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static ax.SpringBootRabbitMQApplication.EXCHANGE_NAME;
import static ax.SpringBootRabbitMQApplication.ROUTING_KEY;

/**
 * @author Steven Gentens
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageSender
{
	private final RabbitTemplate rabbitTemplate;

	public void sendMessage( Events event, String message ) {
		EventMessage eventMessage = EventMessage.builder()
		                                        .event( event )
		                                        .content( message )
		                                        .build();
		log.info( "Sending message {}", eventMessage );
		rabbitTemplate.convertAndSend( EXCHANGE_NAME, ROUTING_KEY, eventMessage );
	}
}
