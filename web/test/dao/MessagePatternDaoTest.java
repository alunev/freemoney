package dao;

import model.MessagePattern;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author red
 * @since 0.0.1
 */
public class MessagePatternDaoTest extends JongoDaoTest {
    private MessagePatternDao patternDao;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        patternDao = app.getWrappedApplication().injector().instanceOf(MessagePatternDao.class);
    }


    @Test
    public void canSavePattern() {
        patternDao.save(ObjectsFactory.createMessagePattern());
    }

    @Test
    public void idIsGeneratedAndSet() {
        MessagePattern pattern = ObjectsFactory.createMessagePattern();
        patternDao.save(pattern);

        assertThat(pattern.get_id()).isNotBlank();
    }

    @Test
    public void canFindByOwnerId() {
        MessagePattern pattern = ObjectsFactory.createMessagePattern();

        patternDao.save(pattern);

        assertThat(patternDao.findByOwnerId("user01").stream().findFirst().get().get_id()).isEqualTo(pattern.get_id());
    }
}