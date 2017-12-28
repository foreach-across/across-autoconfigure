package ax.modules.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Steven Gentens
 */
@Configuration
public class RabbitConfiguration
{
	public final static String EXCHANGE_NAME = "module-exchange";
	public final static String QUEUE_NAME = "module-queue";
	public final static String ROUTING_KEY = "module*";

	@Bean
	public AmqpAdmin amqpAdmin( final ConnectionFactory connectionFactory ) {
		return new RabbitAdmin( connectionFactory );
	}

	@Bean
	public TopicExchange appExchange() {
		return new TopicExchange( EXCHANGE_NAME );
	}

	@Bean
	public Queue appQueueSpecific() {
		return new Queue( QUEUE_NAME );
	}

	@Bean
	public Binding declareBindingSpecific() {
		return BindingBuilder.bind( appQueueSpecific() ).to( appExchange() ).with( ROUTING_KEY );
	}

	@Bean
	public SimpleRabbitListenerContainerFactory customModuleRabbitListenerContainerFactory( ConnectionFactory connectionFactory,
	                                                                                        MessageConverter messageConverter ) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory( connectionFactory );
		factory.setMaxConcurrentConsumers( 1 );
		factory.setMessageConverter( messageConverter );
		factory.setDefaultRequeueRejected( true );
		return factory;
	}
}
