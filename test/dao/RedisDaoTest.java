package dao;

import com.typesafe.config.ConfigFactory;
import org.junit.After;
import org.junit.Before;
import play.Application;
import play.Configuration;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;
import redis.embedded.RedisServer;

/**
 * @author red
 * @since  0.0.1
 */
public abstract class RedisDaoTest extends WithApplication {
    private volatile RedisServer redisServer;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .loadConfig(new Configuration(ConfigFactory.load("application.test.conf")))
                .build();
    }

    @Before
    public void setUp() throws Exception {
//        redisServer = new RedisServer(6379);
//        redisServer.start();
    }

    @After
    public void tearDown() throws Exception {
//        if (redisServer != null) {
//            redisServer.stop();
//        }
    }
}
