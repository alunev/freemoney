package dao;

import model.Account;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author red
 * @since  0.0.1
 */
public class AccountDaoTest extends JongoDaoTest {

    private AccountDao accountDao;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        accountDao = app.getWrappedApplication().injector().instanceOf(AccountDao.class);
    }

    @Test
    public void canSaveWithoutError() throws Exception {
        accountDao.save(ObjectsFactory.createDummyAccount());
        accountDao.save(ObjectsFactory.createDummyAccount());

    }

    @Test
    public void idIsGeneratedAndSet() throws Exception {
        Account account = ObjectsFactory.createDummyAccount();
        accountDao.save(account);

        assertThat(account.getId()).isNotBlank();
    }

    @Test
    public void canFindById() throws Exception {
        accountDao.save(ObjectsFactory.createDummyAccount());
        accountDao.save(ObjectsFactory.createDummyAccount());
        Account account3 = ObjectsFactory.createDummyAccount();
        accountDao.save(account3);
        accountDao.save(ObjectsFactory.createDummyAccount());
        accountDao.save(ObjectsFactory.createDummyAccount());

        assertThat(accountDao.findById(account3.getId())).isEqualTo(account3);
    }

    @Test
    public void canFindByOwnerId() throws Exception {
        accountDao.save(ObjectsFactory.createDummyAccountWithOwnerId("firstUserId"));
        accountDao.save(ObjectsFactory.createDummyAccountWithOwnerId("firstUserId"));
        accountDao.save(ObjectsFactory.createDummyAccountWithOwnerId("firstUserId"));

        accountDao.save(ObjectsFactory.createDummyAccount());
        accountDao.save(ObjectsFactory.createDummyAccount());

        Set<Account> accounts = accountDao.findByOwnerId("firstUserId");

        assertThat(accounts).hasSize(3);
    }
}