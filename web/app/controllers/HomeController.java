package controllers;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import model.User;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;
import views.html.index;
import views.html.login;
import views.html.logon_failed;

import java.util.Optional;

public class HomeController extends Controller {

    private final UserService userService;

    private final Config config;

    @Inject
    public HomeController(UserService userService, Config config) {
        this.userService = userService;
        this.config = config;
    }

    public Result oAuthDenied(String provider, String errorMessage) {
        return ok(logon_failed.render(errorMessage, config));
    }

    public Result index() {
        Optional<User> user = userService.getUser(session());

        if (user.isPresent()) {
            return ok(index.render(user, config));
        } else {
            return ok(login.render("", user, config));
        }
    }

}
