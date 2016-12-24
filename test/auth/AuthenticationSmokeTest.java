package auth;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.ConfigFactory;
import common.TestOverridesWebModule;
import org.junit.*;

import play.Application;
import play.Configuration;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.*;

import static play.test.Helpers.*;
import static org.junit.Assert.*;

public class AuthenticationSmokeTest {

    private Application app;

    @Before
    public void setUp() throws Exception {
        // running(testServer()) loads configuration for second time for some reason, make sure it picks test one
        System.setProperty("config.file", "application.test.conf");

        app = new GuiceApplicationBuilder()
                .overrides(new TestOverridesWebModule())
                .loadConfig(new Configuration(ConfigFactory.load("application.test.conf")))
                .build();
    }

    /**
     * add your integration test here
     * in this example we just check if the welcome page is being shown
     */

    @Test
    public void loginWithGoogleGivesSomeError() {
        running(testServer(3333, app), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:3333/authenticate/google");

            System.out.println(browser.pageSource());
            assertTrue(browser.pageSource().contains("That’s an error."));
        });
    }

}
