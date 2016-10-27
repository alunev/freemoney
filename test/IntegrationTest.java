import com.google.common.collect.Maps;
import org.junit.*;

import play.Application;
import play.mvc.*;
import play.test.*;

import java.util.Map;

import static play.test.Helpers.*;
import static org.junit.Assert.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;

public class IntegrationTest {

    /**
     * add your integration test here
     * in this example we just check if the welcome page is being shown
     */
    @Test
    public void test() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:3333");
            assertTrue(browser.pageSource().contains("Your new application is ready."));
        });
    }

    @Test
    @Ignore
    public void testLoginWithGoogle() {
        Map<String, String> config = Maps.newHashMap(inMemoryDatabase());
        config.put("APP_SECRET", "dummy_secret");

        System.setProperty("APP_SECRET", "dummy_secret");

        Application app = fakeApplication(config);

        running(testServer(3333, app), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:3333/authenticate/google");

            assertTrue(browser.pageSource().contains("Your new application is ready."));
        });
    }

}
