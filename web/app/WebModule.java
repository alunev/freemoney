import com.google.inject.AbstractModule;
import core.LoggingTransactionExecutor;
import core.ParsingTransactionGenerator;
import core.TransactionExecutor;
import core.TransactionGenerator;
import core.message.matcher.AccountMatcher;
import core.message.matcher.AlwaysUndefinedCategoryMatcher;
import core.message.matcher.CategoryMatcher;
import core.message.matcher.RegexAccountMatcher;
import dao.UserDao;
import services.ApplicationTimer;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.time.Clock;

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

        bind(PlayJongo.class).asEagerSingleton();

        bind(UserDao.class).asEagerSingleton();


        bind(TransactionExecutor.class).to(LoggingTransactionExecutor.class);
        bind(AccountMatcher.class).to(RegexAccountMatcher.class);
        bind(TransactionGenerator.class).to(ParsingTransactionGenerator.class);

        bind(CategoryMatcher.class).to(AlwaysUndefinedCategoryMatcher.class);
    }
}
