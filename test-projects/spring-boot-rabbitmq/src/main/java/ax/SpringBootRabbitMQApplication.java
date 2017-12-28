package ax;

import ax.modules.rabbit.CustomRabbitModule;
import com.foreach.across.autoconfigure.rabbitmq.EnableRabbit;
import com.foreach.across.config.AcrossApplication;
import com.foreach.across.modules.web.AcrossWebModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

/**
 * @author Steven Gentens
 */
@AcrossApplication(
		modules = AcrossWebModule.NAME
)
@EnableRabbit
public class SpringBootRabbitMQApplication
{
	public static final String QUEUE_GENERIC_NAME = "appGenericQueue";
	public static final String QUEUE_SPECIFIC_NAME = "appSpecificQueue";
	public static final String EXCHANGE_NAME = "appExchange";
	public static final String ROUTING_KEY = "app*";

	public static void main( String[] args ) {
		SpringApplication.run( SpringBootRabbitMQApplication.class );
	}

	@Bean
	public CustomRabbitModule customRabbitModule() {
		return new CustomRabbitModule();
	}

	@Bean
	public AmqpAdmin amqpAdmin( final ConnectionFactory connectionFactory ) {
		return new RabbitAdmin( connectionFactory );
	}

	@Bean
	public TopicExchange appExchange() {
		return new TopicExchange( EXCHANGE_NAME );
	}

	@Bean
	public Queue appQueueGeneric() {
		return new Queue( QUEUE_GENERIC_NAME );
	}

	@Bean
	public Queue appQueueSpecific() {
		return new Queue( QUEUE_SPECIFIC_NAME );
	}

	@Bean
	public Binding declareBindingGeneric() {
		return BindingBuilder.bind( appQueueGeneric() ).to( appExchange() ).with( ROUTING_KEY );
	}

	@Bean
	public Binding declareBindingSpecific() {
		return BindingBuilder.bind( appQueueSpecific() ).to( appExchange() ).with( ROUTING_KEY );
	}

	@Bean
	public SimpleRabbitListenerContainerFactory myRabbitListenerContainerFactory( ConnectionFactory connectionFactory ) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory( connectionFactory );
		factory.setMaxConcurrentConsumers( 1 );
		factory.setMessageConverter( producerJackson2MessageConverter() );
		factory.setDefaultRequeueRejected( true );
		return factory;
	}

	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
		return new MappingJackson2MessageConverter();
	}

	@Bean
	public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
		DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
		factory.setMessageConverter( consumerJackson2MessageConverter() );
		return factory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate( ConnectionFactory connectionFactory, MessageConverter rabbitJsonMessageConverter ) {
		RabbitTemplate template = new RabbitTemplate( connectionFactory );
		template.setMessageConverter( rabbitJsonMessageConverter );
		return template;
	}
}
