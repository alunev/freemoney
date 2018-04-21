package dao;

import com.typesafe.config.ConfigFactory;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Application;
import play.Configuration;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

/**
 * Created by admin on 5/28/2017.
 */
public class JongoDaoTest extends WithApplication {
    private static final Logger log = LoggerFactory.getLogger(JongoDaoTest.class);

    private MongodProcess mongod;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .loadConfig(new Configuration(ConfigFactory.load("application.test.conf")))
                .build();
    }

    @Before
    public void setUp() throws Exception {
        MongodStarter starter = MongodStarter.getDefaultInstance();

        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net("127.0.0.1", 28017, Network.localhostIsIPv6()))
                .build();

        mongod = starter.prepare(mongodConfig).start();

        log.info("Started MongodProcess " + mongod.getProcessId() + " " + mongod.getConfig().toString());
    }

    @After
    public void tearDown() throws Exception {
        mongod.stop();

        log.info("Stopped MongodProcess " + mongod.getProcessId() + " " + mongod.getConfig().toString());
    }
}
