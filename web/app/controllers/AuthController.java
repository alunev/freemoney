package controllers;

import auth.GoogleSignIn;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import common.SessionParams;
import model.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

/**
 * @author red
 * @since 0.0.1
 */
public class AuthController extends Controller {


    private final GoogleSignIn googleSignIn;

    @Inject
    public AuthController(GoogleSignIn googleSignIn) {
        this.googleSignIn = googleSignIn;
    }

    public Result signOut() {
        session().clear();

        return ok();
    }

    public Result tokenSignIn() {
        Logger.info("Login attempt");

        Optional<String> idTokenString = Optional.ofNullable(request().body().asFormUrlEncoded().get("idtoken"))
                .map(strings -> strings[0]);

        if (!idTokenString.isPresent()) {
            return badRequest("No idtoken found");
        }

        Optional<User> user;
        try {
            user = googleSignIn.processSignInToken(idTokenString.get());
        } catch (GeneralSecurityException | IOException e) {
            return internalServerError("Failed to login", e.toString());
        }

        if (user.isPresent()) {
            session(SessionParams.USER_AUTH_ID, user.get().getAuthId());
            return ok();
        } else {
            return unauthorized("Failed to login");
        }
    }

}
