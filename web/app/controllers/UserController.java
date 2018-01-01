package controllers;

import com.google.inject.Inject;
import play.db.jpa.JPAApi;
import play.mvc.Controller;

public class UserController extends Controller {
    private JPAApi jpaApi;

    @Inject
    public UserController(JPAApi api) {
        jpaApi = api;
    }


}