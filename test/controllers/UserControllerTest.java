package controllers;

import org.junit.Ignore;
import org.junit.Test;
import play.Application;
import play.db.jpa.JPAApi;
import play.mvc.*;
import play.test.*;

import static org.mockito.Mockito.mock;
import static play.test.Helpers.*;
import static org.junit.Assert.*;
import static org.junit.Assert.*;

/**
 * Created by red on 24.09.16.
 */
public class UserControllerTest extends WithApplication {
    @Override
    protected Application provideApplication() {
        System.setProperty("APP_SECRET", "dummy_secret");
        System.setProperty("GOOGLE_OAUTH_CLIENT_ID", "dummy_id");
        System.setProperty("GOOGLE_OAUTH_SECRET", "dummy_secret");

        return fakeApplication();
    }

    @Test
    @Ignore
    public void canPersist() throws Exception {
        new UserController(mock(JPAApi.class)).persist();
    }

}