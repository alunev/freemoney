package controllers;

import org.junit.Test;
import play.mvc.*;
import play.test.*;

import static play.test.Helpers.*;
import static org.junit.Assert.*;
import static org.junit.Assert.*;

/**
 * Created by red on 24.09.16.
 */
public class UserControllerTest extends WithApplication {
    @Test
    public void canPersist() throws Exception {
        new UserController().persist();
    }

}