package dao;

import model.TransactionCategory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author red
 * @since 0.0.1
 */
public class TransactionCategoryDaoTest extends JongoDaoTest {

    private TransactionCategoryDao categoryDao;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        categoryDao = app.getWrappedApplication().injector().instanceOf(TransactionCategoryDao.class);
    }

    @Test
    public void canSave() {
        TransactionCategory category = TransactionCategory.createTransactionCategory("cat 1", "cat 1 desc");

        categoryDao.save(category);

        assertThat(category.get_id()).isNotBlank();
    }

    @Test
    public void canFind() {
        TransactionCategory category = TransactionCategory.createTransactionCategory("cat 1", "cat 1 desc");

        categoryDao.save(category);
        TransactionCategory foundCategory = categoryDao.findById(category.get_id());

        assertThat(foundCategory.identicalTo(category)).isTrue();
    }
}