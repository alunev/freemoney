package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.ConfigFactory;
import common.DateUtils;
import model.Sms;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Application;
import play.Configuration;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.test.WithApplication;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.CompletionStage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static play.mvc.Http.Status.OK;

/**
 * Created by admin on 5/28/2017.
 */
public class RestApiControllerIT extends WithApplication {
    private static final Logger log = LoggerFactory.getLogger(RestApiControllerIT.class);

    private WSClient ws;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .loadConfig(new Configuration(ConfigFactory.load("application.test.conf")))
                .build();
    }

    @Before
    public void setUp() throws Exception {
        ws = app.getWrappedApplication().injector().instanceOf(WSClient.class);
    }

    @Test
    public void sendSomeSmses() throws Exception {
        Sms sms = Sms.createSms(
                "",
                "android-1",
                "",
                "Покупка. Карта *2222. 3344.5 RUB. OKEY. Доступно 12345.92 RUB",
                DateUtils.nowTs());

        JsonNode jsonNode = Json.toJson(sms);

        log.info("Sending json: " + jsonNode.toString());

        CompletionStage<WSResponse> completionStage = ws.url("http://localhost:9000/sms/process")
                .setContentType("application/json")
                .post(jsonNode.toString());

        WSResponse wsResponse = completionStage.toCompletableFuture().get();

        log.info("Response: " + wsResponse.toString());

        assertThat(wsResponse.getStatus(), is(OK));
    }
}