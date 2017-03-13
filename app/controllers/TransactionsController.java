package controllers;

import com.feth.play.module.pa.PlayAuthenticate;
import com.google.inject.Inject;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;
import views.html.transactions;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class TransactionsController extends Controller {

    private final PlayAuthenticate auth;

    private final UserService userService;

    @Inject
    public TransactionsController(PlayAuthenticate auth, UserService userService) {
        this.auth = auth;
        this.userService = userService;
    }

    public Result transactions() {
        return ok(transactions.render(auth, userService.getUser(session())));
    }

}
