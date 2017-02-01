package dao;

import model.Transaction;
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

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        transactionDao = app.getWrappedApplication().injector().instanceOf(TransactionDao.class);
    }

    @Test
    public void canFindById() throws Exception {
        transactionDao.save(Transaction.createTransaction(
                "1",
                "acc1",
                "acc2",
                Currency.getInstance("RUB"),
                Currency.getInstance("USD"),
                BigDecimal.ONE,
                BigDecimal.valueOf(60.0),
                "cat1",
                null,
                null,
                null
        ));

        assertThat("found tx", transactionDao.findById("1"), is(not(nullValue())));
    }

    @Test
    public void save() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }

}