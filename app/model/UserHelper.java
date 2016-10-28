package model;

import com.google.inject.Inject;
import play.db.jpa.JPAApi;


public class UserHelper {
    private final JPAApi jpaApi;

    @Inject
    public UserHelper(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    public User findById(String userId) {
        return jpaApi.withTransaction(em -> em.find(User.class, userId));
    }

}
