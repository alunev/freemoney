package dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import common.DateUtils;
import model.Account;
import model.AppInstance;
import model.Transaction;
import model.TransactionCategory;
import model.User;
import model.UserBuilder;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author red
 * @since 0.0.1
 */
public class UserDaoTest extends JongoDaoTest {

    private UserDao userDao;
    private AccountDao accountDao;
    private TransactionDao transactionDao;
    private TransactionCategoryDao categoryDao;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        userDao = app.getWrappedApplication().injector().instanceOf(UserDao.class);
        accountDao = app.getWrappedApplication().injector().instanceOf(AccountDao.class);
        transactionDao = app.getWrappedApplication().injector().instanceOf(TransactionDao.class);
        categoryDao = app.getWrappedApplication().injector().instanceOf(TransactionCategoryDao.class);
    }

    @Test
    public void canSaveUserWithoutAccounts() throws Exception {
        userDao.save(User.createEmptyUser("google-auth-id", "fred@some_email"));
    }

    @Test
    public void canSaveUserWithoutAccountsAndReadById() throws Exception {
        User fred001 = User.createEmptyUser("google-auth-id", "fred@some_email");
        String id = userDao.save(fred001);

        User user = userDao.findById(id);

        assertTrue("user from db", user.sameAs(fred001));
    }

    @Test
    public void saveOverwritesUserById() throws Exception {
        String id = userDao.save(User.createEmptyUser("google-auth-id", "fred_xxx@some_email"));

        User updatedUser = User.createUser(id, "google-auth-id", "fred@some_email", Collections.emptySet(), Collections.emptySet());
        userDao.save(updatedUser);

        User user = userDao.findById(id);

        assertThat("user from db", user.getEmail(), is(updatedUser.getEmail()));
    }


    @Test
    public void saveAndFindMultipleUsers() throws Exception {
        List<String> ids = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            User user = User.createEmptyUser("google-auth-id", "fred_" + i + "@some_email");
            ids.add(userDao.save(user));
        }

        for (int i = 0; i < 5; i++) {
            User user = User.createUser(ids.get(i), "google-auth-id", "fred_" + i + "@some_email", Collections.emptySet(), Collections.emptySet());
            assertTrue("user from db", userDao.findById(ids.get(i)).sameAs(user));
        }
    }

    @Test
    public void canSaveAndReadUserWithSomeAccountsAndTransactions() throws Exception {
        Account acc1 = ObjectsFactory.createDummyAccountWithOwnerId("user@some_email");
        Account acc2 = ObjectsFactory.createDummyAccountWithOwnerId("user@some_email");
        Account acc3 = ObjectsFactory.createDummyAccountWithOwnerId("user@some_email");

        accountDao.save(acc1);
        accountDao.save(acc2);
        accountDao.save(acc3);

        Set<Account> accounts = Sets.newHashSet(acc1, acc2, acc3);
        User user = User.createUserWithAccounts("user@some_email", accounts);

        userDao.save(user);

        TransactionCategory category = TransactionCategory.createTransactionCategory( "cat 1", "cat 1 desc");
        categoryDao.save(category);

        Transaction tx1 = Transaction.createExpense(
                user.get_id(),
                BigDecimal.valueOf(60.0),
                acc1,
                category,
                DateUtils.now()
        );
        Transaction tx2 = Transaction.createIncome(
                user.get_id(),
                BigDecimal.valueOf(90.0),
                acc1,
                category,
                DateUtils.now()
        );
        Transaction tx3 = Transaction.createTransfer(
                user.get_id(),
                BigDecimal.valueOf(120.0),
                BigDecimal.valueOf(2.0),
                acc1,
                acc2,
                category,
                DateUtils.now()
        );


        transactionDao.save(tx1);
        transactionDao.save(tx2);
        transactionDao.save(tx3);

        user.addTransaction(tx1);
        user.addTransaction(tx2);
        user.addTransaction(tx3);

        String id = userDao.save(user);

        User foundUser = userDao.findById(id);

        assertTrue("found user", foundUser.sameAs(user));
        assertThat("user accounts", foundUser.getAccounts(), hasItems(acc1, acc2, acc3));

        assertThat("user transactions", foundUser.getTransactions(), hasItems(tx1, tx2, tx3));
    }

    @Test
    public void canDeleteUserWithoutAccounts() throws Exception {
        User andy = User.createUserWithAccounts("andy@some_email", Collections.emptySet());

        userDao.save(andy);
        assertThat("found user", userDao.findById(andy.get_id()), is(not(nullValue())));

        String id = andy.get_id();
        userDao.delete(andy);
        assertThat("found user", userDao.findById(id), is(nullValue()));
    }

    @Test
    public void canDeleteUser() throws Exception {
        Set<Account> accounts = Sets.newHashSet(
                ObjectsFactory.createDummyAccountWithOwnerId("1")
        );
        User user = User.createUserWithAccounts("user@some_email", accounts);

        String id = userDao.save(user);

        user = userDao.findById(id);
        assertThat("found user", user, is(not(nullValue())));

        userDao.delete(user);

        assertThat("found user", userDao.findById(id), is(nullValue()));
        assertThat("found account", accountDao.findById(user.get_id()), is(nullValue()));
    }

    @Test
    public void canAddUserAccount() throws Exception {
        Account acc1 = ObjectsFactory.createDummyAccountWithOwnerId("andy");
        Set<Account> accounts = Sets.newHashSet(acc1);

        User andy = User.createUserWithAccounts("andy@some_email", accounts);
        userDao.save(andy);

        andy = userDao.findById(andy.get_id());

        Account acc2 = ObjectsFactory.createDummyAccountWithOwnerId("andy");
        andy.addAccount(acc2);
        userDao.save(andy);

        User foundUser = userDao.findById(andy.get_id());

        assertThat("account 1 present", foundUser.getAccounts(), hasSize(2));
    }

    @Test
    public void canAddAppInstance() throws Exception {
        User user = new UserBuilder().withEmail("user@some_email")
                .withAppInstances(Lists.newArrayList(
                        new AppInstance("abcd", ZonedDateTime.now()),
                        new AppInstance("1234", ZonedDateTime.now())
                ))
                .withAccounts(Collections.emptySet())
                .withTransactions(Collections.emptySet())
                .build();

        userDao.save(user);

        user = userDao.findById(user.get_id());
        user.addAppInstance(new AppInstance("0000", ZonedDateTime.now()));

        userDao.save(user);

        User foundUser = userDao.findById(user.get_id());

        assertThat(foundUser.getAppInstances(), hasSize(3));
    }

    @Test
    public void canModifyUserAccount() throws Exception {
        Account acc1 = ObjectsFactory.createDummyAccountWithOwnerIdAndNumber("user", "11");
        accountDao.save(acc1);

        User user = User.createUserWithAccounts("user@some_email", Sets.newHashSet(acc1));
        userDao.save(user);

        user = userDao.findById(user.get_id());

        Account modified = Account.createAccount(acc1.get_id(), acc1.getOwnerId(), "22", acc1.getTitle(), acc1.getCurrency(), acc1.getBalance(), acc1.getSmsPattern());
        user.removeAccount(acc1);
        user.addAccount(modified);
        userDao.save(user);

        User newUser = userDao.findById(user.get_id());

        assertThat("account was replaced", newUser.getAccounts(), hasSize(1));
        assertThat("account 1 present", newUser.getAccounts(), contains(modified));
        assertThat("modified name", accountDao.findById(modified.get_id()).getNumber(), is("22"));
    }


    @Test
    public void canRemoveUserAccount() throws Exception {
        Account acc2 = ObjectsFactory.createDummyAccountWithOwnerId("andyId");
        accountDao.save(acc2);

        Set<Account> accounts = Sets.newHashSet(
                ObjectsFactory.createDummyAccountWithOwnerId("andyId"),
                acc2
        );

        User andy = User.createUserWithAccounts("andy@some_email", accounts);
        userDao.save(andy);

        andy = userDao.findById(andy.get_id());
        andy.removeAccount(acc2);

        userDao.save(andy);

        User foundUser = userDao.findById(andy.get_id());

        assertThat("user accounts list size", foundUser.getAccounts().size(), is(1));
    }
}