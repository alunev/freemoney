package dao;

import model.Account;
import model.Transaction;
import model.TransactionCategory;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

/**
 * @author red
 * @since 0.0.1
 */
public class TransactionDaoTest extends RedisDaoTest {

    private TransactionDao transactionDao;
    private AccountDao accountDao;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        transactionDao = app.getWrappedApplication().injector().instanceOf(TransactionDao.class);
        accountDao = app.getWrappedApplication().injector().instanceOf(AccountDao.class);
    }

    @Test
    public void canSaveAndFindById() throws Exception {
        Account srcAcc = Account.createAccount("rub001", "user001", "XXX001", "run acc", Currency.getInstance("RUB"),
                BigDecimal.valueOf(12.0), "*XXX001*");
        Account destAcc = Account.createAccount("usd002", "user001", "XXX001", "run acc", Currency.getInstance("USD"),
                BigDecimal.valueOf(12.0), "*XXX002*");

        TransactionCategory category = TransactionCategory.createTransactionCategory("cat001", "cat 1", "cat 1 desc");

        transactionDao.save(Transaction.createTransaction(
                "1",
                "acc1",
                "acc2",
                Currency.getInstance("RUB"),
                Currency.getInstance("USD"),
                BigDecimal.ONE,
                BigDecimal.valueOf(60.0),
                "cat1",
                srcAcc,
                destAcc,
                category
        ));

        Transaction tx = transactionDao.findById("1");

        assertThat("found tx", tx, is(not(nullValue())));
        assertThat("tx src", tx.getSourceAccount(), is(srcAcc));
        assertThat("tx src", tx.getDestAccount(), is(destAcc));
        assertThat("tx src", tx.getCategory(), is(category));
    }

    @Test
    public void canDelete() throws Exception {
        Account srcAcc = Account.createAccount("rub001", "user001", "XXX001", "run acc", Currency.getInstance("RUB"),
                BigDecimal.valueOf(12.0), "*XXX001*");
        Account destAcc = Account.createAccount("usd002", "user001", "XXX001", "run acc", Currency.getInstance("USD"),
                BigDecimal.valueOf(12.0), "*XXX002*");

        TransactionCategory category = TransactionCategory.createTransactionCategory("cat001", "cat 1", "cat 1 desc");

        transactionDao.save(Transaction.createTransaction(
                "1",
                "acc1",
                "acc2",
                Currency.getInstance("RUB"),
                Currency.getInstance("USD"),
                BigDecimal.ONE,
                BigDecimal.valueOf(60.0),
                "cat1",
                srcAcc,
                destAcc,
                category
        ));

        Transaction tx = transactionDao.findById("1");
        assertThat("found tx", tx, is(not(nullValue())));

        transactionDao.delete(tx);

        assertThat("found tx", transactionDao.findById("1"), is(nullValue()));
        assertThat("found src account", accountDao.findById("rub001"), is(nullValue()));
        assertThat("found dest account", accountDao.findById("usd002"), is(nullValue()));
    }

}