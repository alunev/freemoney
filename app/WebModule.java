import auth.FmPlayAuthResolver;
import auth.FmPlayAuthUserService;
import auth.deadbolt.FmPlayAuthCustomDeadboltHook;
import auth.deadbolt.FmPlayAuthDeadboltHandler;
import auth.deadbolt.FmPlayAuthHandlerCache;
import com.feth.play.module.pa.Resolver;
import com.feth.play.module.pa.providers.oauth2.google.GoogleAuthProvider;
import com.google.inject.AbstractModule;

import java.time.Clock;

import dao.UserDao;
import services.ApplicationTimer;
import services.AtomicCounter;
import services.Counter;

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 * <p>
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
public class WebModule extends AbstractModule {

    @Override
    public void configure() {
        // Use the system clock as the default implementation of Clock
        bind(Clock.class).toInstance(Clock.systemDefaultZone());
        // Ask Guice to create an instance of ApplicationTimer when the
        // application starts.
        bind(ApplicationTimer.class).asEagerSingleton();
        // Set AtomicCounter as the implementation for Counter.
        bind(Counter.class).to(AtomicCounter.class);


        bind(UserDao.class).asEagerSingleton();

        bind(Resolver.class).to(FmPlayAuthResolver.class);
        bind(FmPlayAuthUserService.class).asEagerSingleton();
        bind(GoogleAuthProvider.class).asEagerSingleton();

        bind(FmPlayAuthHandlerCache.class).asEagerSingleton();
        bind(FmPlayAuthCustomDeadboltHook.class).asEagerSingleton();
    }
}
