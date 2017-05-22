package dao;

import model.Sms;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * @author red
 * @since 0.0.1
 */
public class SmsDaoTest extends RedisDaoTest {

    private SmsDao smsDao;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        smsDao = app.getWrappedApplication().injector().instanceOf(SmsDao.class);
    }

    @Test
    public void saved() throws Exception {
        Sms sms = Sms.createSms("1", "1", "andr001", "PAY 444.99", DateTime.now());
        smsDao.save(sms);

        assertTrue("read same as saved", smsDao.findById("1").sameAs(sms));
    }
}