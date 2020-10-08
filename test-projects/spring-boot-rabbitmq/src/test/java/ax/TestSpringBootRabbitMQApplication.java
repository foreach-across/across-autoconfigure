package ax;

import ax.application.EventMessage;
import ax.application.Events;
import ax.application.MessageReceiver;
import ax.application.MessageSender;
import ax.modules.rabbit.domain.Message;
import ax.modules.rabbit.domain.MessageType;
import ax.modules.rabbit.receivers.CustomRabbitReceiver;
import ax.modules.rabbit.senders.CustomRabbitSender;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Steven Gentens
 */
public class TestSpringBootRabbitMQApplication extends AbstractIntegrationTest
{
	@Autowired
	private MessageSender sender;

	@Autowired
	private MessageReceiver receiver;

	@Autowired
	private CustomRabbitSender customRabbitSender;

	@Autowired
	private CustomRabbitReceiver customRabbitReceiver;

	@Test
	public void shouldBootstrap() throws InterruptedException {
		sender.sendMessage( Events.FAREWELL, "John" );
		Thread.sleep( 3000 );
		List<EventMessage> messages = receiver.getReceived();
		assertEquals( 1, messages.size() );
		EventMessage message = messages.get( 0 );
		assertEquals( Events.FAREWELL, message.getEvent() );
		assertEquals( "John", message.getContent() );

		customRabbitSender.send( MessageType.RED, "Red" );
		Thread.sleep( 3000 );
		List<Message> customMessages = customRabbitReceiver.getReceived();
		assertEquals( 1, customMessages.size() );
		Message customMessage = customMessages.get( 0 );
		assertEquals( MessageType.RED, customMessage.getType() );
		assertEquals( "Red", customMessage.getContent() );
	}
}
