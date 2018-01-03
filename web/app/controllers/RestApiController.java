package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import dao.SmsDao;
import model.Sms;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.UUID;

/**
 * @author red
 * @since 0.0.1
 */
public class RestApiController extends Controller{

    private SmsDao smsDao;

    @Inject
    public RestApiController(SmsDao smsDao) {
        this.smsDao = smsDao;
    }

    public Result processSms() {
        JsonNode jsonNode = request().body().asJson();

        Sms sms = Json.fromJson(jsonNode, Sms.class);

        smsDao.save(sms);

        return ok();
    }
}
