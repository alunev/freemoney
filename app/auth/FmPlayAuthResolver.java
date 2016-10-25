package auth;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.Resolver;
import com.feth.play.module.pa.exceptions.AccessDeniedException;
import com.feth.play.module.pa.exceptions.AuthException;
import com.feth.play.module.pa.providers.oauth2.google.GoogleAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import controllers.routes;
import model.User;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

import static play.mvc.Http.Context.Implicit.session;

/**
 * Concrete Resolver implementation.
 */
@Singleton
public class FmPlayAuthResolver extends Resolver {

    public FmPlayAuthResolver() {

    }

    @Override
    public Call login() {
        // Your login page
        return controllers.routes.HomeController.index();
    }

    @Override
    public Call afterAuth() {
        play.api.mvc.Call index = controllers.routes.HomeController.index();


        return index;
    }

    @Override
    public Call afterLogout() {
        return controllers.routes.HomeController.index();
    }

    @Override
    public Call auth(final String provider) {
        // You can provide your own authentication implementation,
        // however the default should be sufficient for most cases
        return com.feth.play.module.pa.controllers.routes.Authenticate.authenticate(provider);
    }

    @Override
    public Call onException(final AuthException e) {
        if (e instanceof AccessDeniedException) {
            AccessDeniedException accessDeniedException = (AccessDeniedException) e;
            return controllers.routes.HomeController.oAuthDenied(
                    accessDeniedException.getProviderKey(),
                    accessDeniedException.toString()
            );
        }

        // more custom problem handling here...

        return super.onException(e);
    }

    @Override
    public Call askLink() {
        // We don't support moderated account linking in this sample.
        // See the play-authenticate-usage project for an example
        return null;
    }

    @Override
    public Call askMerge() {
        // We don't support moderated account merging in this sample.
        // See the play-authenticate-usage project for an example
        return null;
    }
}
