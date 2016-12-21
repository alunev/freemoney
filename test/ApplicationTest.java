import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.Resolver;
import com.typesafe.config.ConfigFactory;
import model.User;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.Configuration;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.test.WithApplication;
import play.twirl.api.Content;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ApplicationTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        System.setProperty("APP_SECRET", "dummy");
        System.setProperty("GOOGLE_OAUTH_SECRET", "dummy");
        System.setProperty("GOOGLE_OAUTH_CLIENT_ID", "dummy");

        return new GuiceApplicationBuilder()
                .overrides(new TestOverridesWebModule())
                .build();
    }

    @Before
    public void setUp() throws Exception {
        Http.Context context = mock(Http.Context.class);

        //mocking flash session, request, etc... as required
        when(context.flash()).thenReturn(mock(Http.Flash.class));
        when(context.session()).thenReturn(mock(Http.Session.class));

        Http.Context.current.set(context);
    }

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertEquals(2, a);
    }

    @Test
    public void renderTemplate() {
        PlayAuthenticate playAuth = new PlayAuthenticate(
                new Configuration(ConfigFactory.load("play-authenticate/mine.conf")),
                mock(Resolver.class)
        );

        Content html = views.html.index.render(playAuth, User.GUEST);

        assertEquals("text/html", html.contentType());
        assertTrue(html.body().contains("Login with google"));
    }


}
