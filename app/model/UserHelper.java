package model;

import com.google.inject.Inject;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;


public class UserHelper {
    private final JPAApi jpaApi;

    @Inject
    public UserHelper(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    public User findById(String userId) {
        EntityManager em = jpaApi.em();

        return em.find(User.class, userId);
    }

}
