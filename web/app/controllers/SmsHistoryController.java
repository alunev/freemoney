package controllers;

import com.feth.play.module.pa.PlayAuthenticate;
import com.google.inject.Inject;
import dao.SmsDao;
import model.User;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;

public class SmsHistoryController extends Controller {

    private final PlayAuthenticate auth;

    private final UserService userService;

    private final SmsDao smsDao;

    private final views.html.sms_history sms_history;

    @Inject
    public SmsHistoryController(PlayAuthenticate auth,
                                UserService userService,
                                SmsDao smsDao,
                                views.html.sms_history sms_history) {
        this.auth = auth;
        this.userService = userService;
        this.smsDao = smsDao;
        this.sms_history = sms_history;
    }

    public Result list() {
        User user = userService.getUser(session());

        return ok(sms_history.render(auth, user, smsDao.findByOwnerId(user.getId())));
    }


}
