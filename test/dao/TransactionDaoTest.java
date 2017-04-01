package dao;

import model.Account;
import model.Transaction;
import model.TransactionCategory;
import model.TransactionType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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

        transactionDao.save(Transaction.createTransfer(
                "1",
                BigDecimal.ONE,
                BigDecimal.valueOf(60.0),
                srcAcc,
                destAcc,
                category,
                DateTime.now(DateTimeZone.UTC)
        ));

        Transaction tx = transactionDao.findById("1");

        assertThat("found tx", tx, is(not(nullValue())));
        assertThat("tx type", tx.getTransactionType(), is(TransactionType.TRANSFER));
        assertThat("tx src", tx.getSourceAccount(), is(srcAcc));
        assertThat("tx dest", tx.getDestAccount(), is(destAcc));
        assertThat("tx category", tx.getCategory(), is(category));
    }

    @Test
    public void canDelete() throws Exception {
        Account srcAcc = Account.createAccount("rub001", "user001", "XXX001", "run acc", Currency.getInstance("RUB"),
                BigDecimal.valueOf(12.0), "*XXX001*");
        Account destAcc = Account.createAccount("usd002", "user001", "XXX001", "run acc", Currency.getInstance("USD"),
                BigDecimal.valueOf(12.0), "*XXX002*");

        TransactionCategory category = TransactionCategory.createTransactionCategory("cat001", "cat 1", "cat 1 desc");

        transactionDao.save(Transaction.createTransfer(
                "1",
                BigDecimal.ONE,
                BigDecimal.valueOf(60.0),
                srcAcc,
                destAcc,
                category,
                DateTime.now(DateTimeZone.UTC)
        ));

        transactionDao.save(Transaction.createExpense(
                "2",
                BigDecimal.ONE,
                srcAcc,
                category,
                DateTime.now(DateTimeZone.UTC)
        ));

        Transaction tx1 = transactionDao.findById("1");
        assertThat("found tx1", tx1, is(not(nullValue())));
        assertThat("tx1 type", tx1.getTransactionType(), is(TransactionType.TRANSFER));

        Transaction tx2 = transactionDao.findById("2");
        assertThat("found tx2", tx2, is(not(nullValue())));
        assertThat("tx2 type", tx2.getTransactionType(), is(TransactionType.EXPENSE));

        transactionDao.delete(tx1);

        assertThat("found tx1", transactionDao.findById("1"), is(nullValue()));
        assertThat("found src account", accountDao.findById("rub001"), is(nullValue()));
        assertThat("found dest account", accountDao.findById("usd002"), is(nullValue()));
    }

}