package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.ConfigFactory;
import common.TestOverridesWebModule;
import core.TransactionExecutor;
import core.TransactionGenerator;
import dao.SmsDao;
import dao.TransactionDao;
import model.Sms;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import play.Application;
import play.Configuration;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static play.inject.Bindings.bind;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.route;

/**
 * Created by admin on 5/28/2017.
 */
public class RestApiControllerTest extends WithApplication {

    private SmsDao smsDao;

    @Override
    protected Application provideApplication() {
        smsDao = mock(SmsDao.class);

        return new GuiceApplicationBuilder()
                .overrides(new TestOverridesWebModule())
                .loadConfig(new Configuration(ConfigFactory.load("application.test.no.mongo.conf")))
                .overrides(bind(SmsDao.class).toInstance(smsDao))
                .overrides(bind(TransactionGenerator.class).toInstance(mock(TransactionGenerator.class)))
                .overrides(bind(TransactionDao.class).toInstance(mock(TransactionDao.class)))
                .build();
    }

    @Test
    public void savesSmsFine() throws Exception {
        Sms sms = Sms.createSms(
                "owner123",
                "android-1",
                "",
                "CASH 45.12 CAFE",
                ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());

        JsonNode jsonNode = Json.toJson(sms);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method("POST")
                .bodyJson(jsonNode)
                .uri(controllers.routes.RestApiController.processSms().url());

        Result result = route(request);

        ArgumentCaptor<Sms> captor = ArgumentCaptor.forClass(Sms.class);
        verify(smsDao, times(1)).save(captor.capture());

        assertThat("", captor.getValue().getText(), equalTo("CASH 45.12 CAFE"));

        assertThat(result.status(), is(OK));
    }
}