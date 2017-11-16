package ax;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BlankApplication.class )
public class TestBlankApplication {
    @Value("${local.server.port}")
    private int port;

    @Test
    public void shouldBootstrapAndReturnWhitelabelErrorPage() throws IOException {
        Content content = Request.Get( "http://localhost:" + port ).addHeader(new BasicHeader("Accept", "text/html")).execute().handleResponse(new ResponseHandler<Content>() {
            public Content handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                final HttpEntity entity = response.getEntity();
                return entity == null ? null : new Content(EntityUtils.toByteArray(entity), ContentType.getOrDefault(entity));
            }
        });
        assertNotNull( content );
        String body = content.asString();
        assertTrue( "Body should contain whitelabel spring boot error page", StringUtils.contains( body,"<html><body><h1>Whitelabel Error Page</h1><p>This application has no explicit mapping for /error, so you are seeing this as a fallback."));

    }
}
