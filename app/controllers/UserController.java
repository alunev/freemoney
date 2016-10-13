package controllers;

import com.google.inject.Inject;
import model.User;
import play.db.jpa.JPAApi;
import play.mvc.Controller;
import play.mvc.Result;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class UserController extends Controller {
    private JPAApi jpaApi;

    @Inject
    public UserController(JPAApi api) {
        jpaApi = api;
    }

    public Result persist() {
        EntityManager em = jpaApi.em();

        User user = User.createUser("0001", "tes@gmail.com");
        em.persist(user); // persist object.

        User result = (User) em.find(User.class, "0001");

        return ok("User 0001 record persisted for persistence unit cassandra_pu");
    }

    public Result find() {
        EntityManager em = jpaApi.em();

        User user = em.find(User.class, "0001");
        em.close();
        return ok("Found User in database with the following details:" + printUser(user));
    }

    public Result update() {
        EntityManager em = jpaApi.em();

        User user = em.find(User.class, "0001");

        User updatedUser = User.copyOf(user);

        em.merge(user);
        user = em.find(User.class, "0001");

        return ok("Record updated:" + printUser(user));
    }

    public Result delete() {
        EntityManager em = jpaApi.em();

        User user = em.find(User.class, "0001");

        em.remove(user);
        user = em.find(User.class, "0001");
        return ok("Record deleted:" + printUser(user));
    }

    private static String printUser(User user) {
        if (user == null) {
            return "Record not found";
        }

        return user.toString();
    }
}