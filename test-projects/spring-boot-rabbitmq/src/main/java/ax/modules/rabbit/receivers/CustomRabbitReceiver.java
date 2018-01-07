package ax.modules.rabbit.receivers;

import ax.modules.rabbit.config.RabbitConfiguration;
import ax.modules.rabbit.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steven Gentens
 */
@Service
@RequiredArgsConstructor
public class CustomRabbitReceiver
{
	private List<Message> received = new ArrayList<>();

	@RabbitListener(containerFactory = "customModuleRabbitListenerContainerFactory", queues = RabbitConfiguration.QUEUE_NAME, exclusive = true, id = "customRabbitReceiver-queue")
	public void receiveMessage( final Message customMessage ) {
		received.add( customMessage );
	}

	public List<Message> getReceived() {
		return received;
	}
}
