package controllers;

import com.typesafe.config.ConfigFactory;
import common.TestOverridesWebModule;
import org.junit.Ignore;
import org.junit.Test;
import play.Application;
import play.Configuration;
import play.db.jpa.JPAApi;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import static org.mockito.Mockito.mock;

/**
 * Created by red on 24.09.16.
 */
public class UserControllerTest extends WithApplication {
    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .overrides(new TestOverridesWebModule())
                .loadConfig(new Configuration(ConfigFactory.load("application.test.conf")))
                .build();
    }

    @Test
    public void canPersist() throws Exception {
        new UserController(mock(JPAApi.class)).persist();
    }

}