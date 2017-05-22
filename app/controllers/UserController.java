package controllers;

import com.google.inject.Inject;
import model.User;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.persistence.EntityManager;

public class UserController extends Controller {
    private JPAApi jpaApi;

    @Inject
    public UserController(JPAApi api) {
        jpaApi = api;
    }


}