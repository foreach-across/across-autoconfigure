package ax.application;

import ax.SpringBootRabbitMQApplication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steven Gentens
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageReceiver
{
	private List<EventMessage> received = new ArrayList<>();
	private final RabbitTemplate rabbitTemplate;
	private final MessageSender sender;

	@RabbitListener(containerFactory = "myRabbitListenerContainerFactory", queues = SpringBootRabbitMQApplication.QUEUE_GENERIC_NAME, exclusive = true, id = "messageReceiver-queue-generic")
	public void receiveMessage( final Message message ) {
		log.info( "Received message as generic: {}", message.toString() );
	}

	@RabbitListener(containerFactory = "myRabbitListenerContainerFactory", queues = SpringBootRabbitMQApplication.QUEUE_SPECIFIC_NAME, exclusive = true, id = "messageReceiver-queue-specific")
	public void receiveMessage( final EventMessage eventMessage ) {
		received.add( eventMessage );
		log.info( "Received message as specific class: {}", eventMessage.toString() );
		if ( eventMessage.getEvent() == Events.HELLO ) {
			sender.sendMessage( Events.HELLO_RESPONSE, "Hello " + eventMessage.getContent() );

			rabbitTemplate.convertAndSend( SpringBootRabbitMQApplication.QUEUE_GENERIC_NAME, "Bye " + eventMessage.getContent() );
			rabbitTemplate.convertAndSend( SpringBootRabbitMQApplication.QUEUE_SPECIFIC_NAME,
			                               EventMessage.builder().content( "Bye " + eventMessage.getContent() )
			                                           .event( Events.FAREWELL )
			                                           .build() );
		}
	}

	public List<EventMessage> getReceived() {
		return received;
	}
}
