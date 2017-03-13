package controllers;

import com.feth.play.module.pa.PlayAuthenticate;
import com.google.inject.Inject;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;
import views.html.accounts;

public class AccountsController extends Controller {

    private final PlayAuthenticate auth;

    private final UserService userService;

    @Inject
    public AccountsController(PlayAuthenticate auth, UserService userService) {
        this.auth = auth;
        this.userService = userService;
    }

    public Result accounts() {
        return ok(accounts.render(auth, userService.getUser(session())));
    }

}
