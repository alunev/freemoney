package controllers;

import com.feth.play.module.pa.PlayAuthenticate;
import com.google.inject.Inject;
import model.User;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;
import views.html.index;
import views.html.logon_failed;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private final PlayAuthenticate auth;

    private final UserService userService;

    @Inject
    public HomeController(PlayAuthenticate auth, UserService userService) {
        this.auth = auth;
        this.userService = userService;
    }

    public Result oAuthDenied(String provider, String errorMessage) {
        return ok(logon_failed.render(auth, User.GUEST, errorMessage));
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        User user = userService.getUser(session());

        if (user != null) {
            return ok(index.render(auth, user));
        } else {
            return redirect(com.feth.play.module.pa.controllers.routes.Authenticate.logout());
        }
    }

}
