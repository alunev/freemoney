package controllers;

import com.google.inject.Inject;
import model.User;
import org.pac4j.play.java.Secure;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;

import java.util.Optional;

/**
 * @author red
 * @since 0.0.1
 */
public class AuthController extends Controller {


    private final UserService userService;

    @Inject
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Secure(clients = "Google2Client")
    public Result loginGoogle() {
        Optional<User> user = userService.getUser();

        return ok(views.html.index.render(user));
    }

}
