package dao;

import common.DateUtils;
import model.Account;
import model.Transaction;
import model.TransactionCategory;
import model.TransactionType;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

/**
 * @author red
 * @since 0.0.1
 */
public class TransactionDaoTest extends JongoDaoTest {

    private TransactionDao transactionDao;
    private AccountDao accountDao;
    private TransactionCategoryDao categoryDao;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        transactionDao = app.getWrappedApplication().injector().instanceOf(TransactionDao.class);
        accountDao = app.getWrappedApplication().injector().instanceOf(AccountDao.class);
        categoryDao = app.getWrappedApplication().injector().instanceOf(TransactionCategoryDao.class);
    }

    @Test
    public void canSaveAndFindById() throws Exception {
        Account srcAcc = Account.createAccount(
                "user001",
                "rub001",
                "run acc",
                Currency.getInstance("RUB"),
                BigDecimal.valueOf(12.0),
                "*XXX001*"
        );
        Account destAcc = Account.createAccount(
                "usd002",
                "user001",
                "run acc",
                Currency.getInstance("USD"),
                BigDecimal.valueOf(12.0),
                "*XXX002*"
        );

        TransactionCategory category = TransactionCategory.createTransactionCategory("cat 1", "cat 1 desc");

        accountDao.save(srcAcc);
        accountDao.save(destAcc);
        categoryDao.save(category);

        Transaction transaction = Transaction.createTransfer(
                "test_owner",
                BigDecimal.ONE,
                BigDecimal.valueOf(60.0),
                srcAcc,
                destAcc,
                category,
                DateUtils.now()
        );
        transactionDao.save(transaction);

        Transaction tx = transactionDao.findById(transaction.get_id());

        assertThat("found tx", tx, is(not(nullValue())));
        assertThat("tx type", tx.getTransactionType(), is(TransactionType.TRANSFER));
        assertThat("tx src", tx.getSourceId(), is(srcAcc.get_id()));
        assertThat("tx dest", tx.getDestId(), is(destAcc.get_id()));
        assertThat("tx category", tx.getCategory(), is(category));
    }

    @Test
    public void canSaveAndFindByOwnerId() throws Exception {
        Account srcAcc = Account.createAccount("rub001",
                "user001",
                "XXX001",
                Currency.getInstance("RUB"),
                BigDecimal.valueOf(12.0),
                "*XXX001*"
        );
        Account destAcc = Account.createAccount("usd002",
                "user001",
                "XXX001",
                Currency.getInstance("USD"),
                BigDecimal.valueOf(12.0),
                "*XXX002*"
        );

        TransactionCategory category = TransactionCategory.createTransactionCategory("cat 1", "cat 1 desc");

        accountDao.save(srcAcc);
        accountDao.save(destAcc);
        categoryDao.save(category);

        transactionDao.save(Transaction.createTransfer(
                "test_owner",
                BigDecimal.ONE,
                BigDecimal.valueOf(60.0),
                srcAcc,
                destAcc,
                category,
                DateUtils.now()
        ));

        transactionDao.save(Transaction.createTransfer(
                "test_owner",
                BigDecimal.ONE,
                BigDecimal.valueOf(60.0),
                srcAcc,
                destAcc,
                category,
                DateUtils.now()
        ));

        Set<Transaction> txList = transactionDao.findByOwnerId("test_owner");

        txList.forEach(tx -> {
            assertThat("found tx", tx, is(not(nullValue())));
            assertThat("tx type", tx.getTransactionType(), is(TransactionType.TRANSFER));
            assertThat("tx src", tx.getSourceId(), is(srcAcc.get_id()));
            assertThat("tx dest", tx.getDestId(), is(destAcc.get_id()));
            assertThat("tx category", tx.getCategory(), is(category));
        });

    }

    @Test
    public void canDelete() throws Exception {
        Account srcAcc = Account.createAccount(
                "user001",
                "rub001",
                "XXX001",
                Currency.getInstance("RUB"),
                BigDecimal.valueOf(12.0),
                "*XXX001*"
        );
        Account destAcc = Account.createAccount(
                "user001",
                "usd002",
                "XXX001",
                Currency.getInstance("USD"),
                BigDecimal.valueOf(12.0),
                "*XXX002*"
        );

        TransactionCategory category = TransactionCategory.createTransactionCategory("cat 1", "cat 1 desc");

        accountDao.save(srcAcc);
        accountDao.save(destAcc);
        categoryDao.save(category);

        Transaction transfer = Transaction.createTransfer(
                "test_owner",
                BigDecimal.ONE,
                BigDecimal.valueOf(60.0),
                srcAcc,
                destAcc,
                category,
                DateUtils.now()
        );
        transactionDao.save(transfer);

        Transaction expense = Transaction.createExpense(
                "test_owner",
                BigDecimal.ONE,
                srcAcc,
                category,
                DateUtils.now()
        );
        transactionDao.save(expense);

        Transaction tx1 = transactionDao.findById(transfer.get_id());
        assertThat("found tx1", tx1, is(not(nullValue())));
        assertThat("tx1 type", tx1.getTransactionType(), is(TransactionType.TRANSFER));

        Transaction tx2 = transactionDao.findById(expense.get_id());
        assertThat("found tx2", tx2, is(not(nullValue())));
        assertThat("tx2 type", tx2.getTransactionType(), is(TransactionType.EXPENSE));

        transactionDao.delete(tx1);

        assertThat("found tx1", transactionDao.findById(transfer.get_id()), is(nullValue()));
        assertThat("found src account", accountDao.findById(srcAcc.get_id()), is(notNullValue()));
        assertThat("found dest account", accountDao.findById(destAcc.get_id()), is(notNullValue()));
    }

}