package common;

import auth.FmPlayAuthResolver;
import auth.FmPlayAuthUserService;
import auth.deadbolt.FmPlayAuthDeadboltHandler;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.models.Subject;
import com.feth.play.module.pa.Resolver;
import com.feth.play.module.pa.providers.oauth2.google.GoogleAuthProvider;
import com.google.inject.AbstractModule;
import core.LoggingTransactionExecutor;
import core.TransactionExecutor;
import core.message.matcher.AccountMatcher;
import dao.UserDao;
import model.User;
import play.api.mvc.Call;
import play.db.jpa.JPAApi;
import play.mvc.Http;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by red on 19.11.16.
 */
public class TestOverridesWebModule extends AbstractModule {
    @Override
    protected void configure() {
        FmPlayAuthResolver authResolver = mock(FmPlayAuthResolver.class);
        UserDao userDao = mock(UserDao.class);
        User testUser = User.createEmptyUser("user_some_auth_id", "user@some_email");
        FmPlayAuthDeadboltHandler deadboltHandler = mock(FmPlayAuthDeadboltHandler.class);

        when(authResolver.auth(anyString())).thenReturn(new Call("GET", "google_url", "fragment"));
        when(userDao.findByAuthId(anyString())).thenReturn(testUser);
        when(deadboltHandler.getSubject(any(Http.Context.class))).thenReturn(
                CompletableFuture.completedFuture(Optional.ofNullable((Subject) testUser))
        );

        bind(GoogleAuthProvider.class).asEagerSingleton();
        bind(JPAApi.class).toInstance(mock(JPAApi.class));
        bind(UserDao.class).toInstance(userDao);
        bind(Resolver.class).toInstance(authResolver);
        bind(FmPlayAuthUserService.class).toInstance(mock(FmPlayAuthUserService.class));
        bind(DeadboltHandler.class).toInstance(deadboltHandler);
        bind(TransactionExecutor.class).to(LoggingTransactionExecutor.class);
        bind(AccountMatcher.class).toInstance(mock(AccountMatcher.class));


//        bind(GoogleAuthProvider.class).asEagerSingleton();

//        bind(FmPlayAuthHandlerCache.class).toInstance(mock(FmPlayAuthHandlerCache.class));
//        bind(FmPlayAuthCustomDeadboltHook.class).toInstance(mock(FmPlayAuthCustomDeadboltHook.class));
    }
}
