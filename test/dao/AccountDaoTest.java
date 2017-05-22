package dao;

import model.Account;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author red
 * @since  0.0.1
 */
public class AccountDaoTest extends RedisDaoTest {

    private AccountDao accountDao;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        accountDao = app.getWrappedApplication().injector().instanceOf(AccountDao.class);
    }

    @Test
    public void canSaveWithoutError() throws Exception {
        accountDao.save(ObjectsFactory.createDummyAccountWithId("11"));
        accountDao.save(ObjectsFactory.createDummyAccountWithId("22"));

    }

    @Test
    public void canFindById() throws Exception {
        accountDao.save(ObjectsFactory.createDummyAccountWithId("1"));
        accountDao.save(ObjectsFactory.createDummyAccountWithId("2"));
        Account account3 = ObjectsFactory.createDummyAccountWithId("3");
        accountDao.save(account3);
        accountDao.save(ObjectsFactory.createDummyAccountWithId("4"));
        accountDao.save(ObjectsFactory.createDummyAccountWithId("5"));

        assertThat("found account", accountDao.findById("3"), is(account3));
    }

    @Test
    public void canFindByOwnerId() throws Exception {
        accountDao.save(ObjectsFactory.createDummyAccountWithIdAndOwnerId("1", "firstUserId"));
        accountDao.save(ObjectsFactory.createDummyAccountWithIdAndOwnerId("2", "firstUserId"));
        accountDao.save(ObjectsFactory.createDummyAccountWithIdAndOwnerId("3", "firstUserId"));

        accountDao.save(ObjectsFactory.createDummyAccountWithId("4"));
        accountDao.save(ObjectsFactory.createDummyAccountWithId("5"));

        Set<Account> accounts = accountDao.findByOwnerId("firstUserId");

        assertThat("found account", accounts.size(), is(3));
    }
}