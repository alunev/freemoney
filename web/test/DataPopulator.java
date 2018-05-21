import com.typesafe.config.ConfigFactory;
import common.DateUtils;
import dao.AccountDao;
import dao.MessagePatternDao;
import dao.ObjectsFactory;
import dao.TransactionCategoryDao;
import dao.TransactionDao;
import dao.UserDao;
import model.Account;
import model.MessagePattern;
import model.Transaction;
import model.TransactionCategory;
import model.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import play.Application;
import play.Configuration;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author red
 * @since 0.0.1
 */

@Ignore
public class DataPopulator extends WithApplication {

    private AccountDao accountDao;
    private UserDao userDao;
    private TransactionCategoryDao transactionCategoryDao;
    private TransactionDao transactionDao;
    private MessagePatternDao patternDao;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .loadConfig(new Configuration(ConfigFactory.load("application.test.conf")))
                .build();
    }

    @Before
    public void setUp() {
        accountDao = app.getWrappedApplication().injector().instanceOf(AccountDao.class);
        userDao = app.getWrappedApplication().injector().instanceOf(UserDao.class);
        transactionCategoryDao = app.getWrappedApplication().injector().instanceOf(TransactionCategoryDao.class);
        transactionDao = app.getWrappedApplication().injector().instanceOf(TransactionDao.class);
        patternDao = app.getWrappedApplication().injector().instanceOf(MessagePatternDao.class);
    }


    @Test
    public void fillBasicData() {
        populatePatterns();
        populateUserWithSingleAccoutAndSomeTransactions();
    }

    @Test
    public void populatePatterns() {
        patternDao.save(new MessagePattern(
                "01",
                "Покупка\\. Карта \\*(\\d{4}). (\\d+\\.\\d+) RUB. OKEY. Доступно (\\d+\\.\\d+) RUB",
                "Tinkoff",
                DateUtils.now()
        ));
    }

    @Test
    public void populateUserWithSingleAccoutAndSomeTransactions() {
        TransactionCategory foodCat = TransactionCategory.createTransactionCategory("Food",
                "all kinds of food");
        TransactionCategory flatCat = TransactionCategory.createTransactionCategory("Flat", "commodities");
        TransactionCategory transportCat = TransactionCategory.createTransactionCategory("Transaport", "tickets etc.");

        transactionCategoryDao.save(foodCat);
        transactionCategoryDao.save(flatCat);
        transactionCategoryDao.save(transportCat);

        User user = User.createEmptyUser("", "user@some_email");

        userDao.save(user);

        Account account = ObjectsFactory.createDummyAccountWithOwnerId(user.getId());
        accountDao.save(account);

        user.addAccount(account);

        transactionDao.save(Transaction.createExpense(
                user.getId(), BigDecimal.valueOf(0.5), account, transactionCategoryDao.findById(foodCat.getId()), DateUtils
                        .now()));
        transactionDao.save(Transaction.createExpense(
                user.getId(), BigDecimal.valueOf(23.5), account, transactionCategoryDao.findById(foodCat.getId()), DateUtils.now()));
        transactionDao.save(Transaction.createExpense(
                user.getId(), BigDecimal.valueOf(645.5), account, transactionCategoryDao.findById(foodCat.getId()), DateUtils.now()));
        transactionDao.save(Transaction.createExpense(
                user.getId(), BigDecimal.valueOf(6.5), account, transactionCategoryDao.findById(flatCat.getId()), DateUtils.now()));
        transactionDao.save(Transaction.createExpense(
                user.getId(), BigDecimal.valueOf(644.35), account, transactionCategoryDao.findById(transportCat.getId()), DateUtils.now()));

    }
}
