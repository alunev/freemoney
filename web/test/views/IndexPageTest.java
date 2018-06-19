package views;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import common.TestOverridesWebModule;
import model.User;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.Configuration;
import play.api.mvc.RequestHeader;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.test.WithApplication;
import play.twirl.api.Content;

import java.util.Optional;

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
public class IndexPageTest extends WithApplication {

    private Http.Context context;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .overrides(new TestOverridesWebModule())
                .loadConfig(new Configuration(ConfigFactory.load("application.test.conf")))
                .build();
    }

    @Before
    public void setUp() throws Exception {
        context = mock(Http.Context.class);

        // mocking flash session, request, etc... as required
        when(context.flash()).thenReturn(mock(Http.Flash.class));
        when(context.session()).thenReturn(mock(Http.Session.class));

        Http.Context.current.set(context);
    }

    @Test
    public void indexTemplateContainsLoginLink() {
        when(context.request()).thenReturn(mock(Http.Request.class));
        when(context._requestHeader()).thenReturn(mock(RequestHeader.class));

        Content html = views.html.index.render(Optional.empty(), mock(Config.class));

        assertEquals("text/html", html.contentType());
        assertTrue(html.body().contains("Login with google"));
    }

    @Test
    public void indexPageContainsAccountsLinkIfLoggedIn() {
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(context._requestHeader()).thenReturn(requestHeader);

        Content html = views.html.index.render(
                java.util.Optional.ofNullable(User.createEmptyUser("user_some_auth_id", "user@some_email")),
                mock(Config.class)
        );

        assertEquals("text/html", html.contentType());
        assertTrue(html.body().contains("Accounts"));
    }
}
