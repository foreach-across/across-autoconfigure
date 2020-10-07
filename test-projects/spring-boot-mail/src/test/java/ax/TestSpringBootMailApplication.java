package ax;

import com.icegreen.greenmail.util.GreenMail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootMailApplication.class)
public class TestSpringBootMailApplication
{
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailProperties mailProperties;

	@Autowired
	private ObjectProvider<Session> session;

	@Autowired
	private GreenMail smtpServer;

	@Test
	public void shouldBootstrapAndSendMail() throws MessagingException {
		assertNotNull( mailSender );
		assertNotNull( mailProperties );
		assertNotNull( session );

		final MimeMessage mimeMessage = mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper( mimeMessage, "UTF-8" );
		message.setFrom( "sender@example.com" );
		message.setTo( "recipient@example.com" );
		message.setSubject( "This is the message subject" );
		message.setText( "This is the message body" );
		mailSender.send( mimeMessage );

		MimeMessage[] messages = smtpServer.getReceivedMessages();
		assertNotNull( messages );
		assertEquals( 1, messages.length );
	}
}
