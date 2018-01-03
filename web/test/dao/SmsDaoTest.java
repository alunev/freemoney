package dao;

import model.Sms;
import org.junit.Before;
import org.junit.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertTrue;

/**
 * @author red
 * @since 0.0.1
 */
public class SmsDaoTest extends JongoDaoTest {

    private SmsDao smsDao;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        smsDao = app.getWrappedApplication().injector().instanceOf(SmsDao.class);
    }

    @Test
    public void saved() throws Exception {
        Sms sms = Sms.createSms("1", "andr001", "PAY 444.99", ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        smsDao.save(sms);

        assertTrue("read same as saved", smsDao.findById(sms.getId()).sameAs(sms));
    }
}