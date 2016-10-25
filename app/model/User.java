package model;


import com.google.inject.Inject;
import play.api.Play;
import play.db.jpa.JPAApi;

import javax.persistence.*;

@Entity
@Table(name = "users", schema = "RedisK@redis_pu")
public class User {
    private final JPAApi jpaApi;

    private final UserHelper userHelper;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "email")
    private String email;

    @Inject
    public User(JPAApi api, UserHelper userHelper) {
        this.jpaApi = api;
        this.userHelper = userHelper;
    }

    public static User copyOf(User user) {
        User user1 = new User(user.jpaApi, user.userHelper);

        user1.setUserId(user.getUserId());
        user1.setEmail(user.getEmail());

        return user1;
    }

    public static User createUser(String userId) {
        return createUser(userId, "");
    }

    public static User createUser(String userId, String email) {
        User user = Play.current().injector().instanceOf(User.class);

        user.userId = userId;
        user.email = email;

        return user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void save() {
        EntityManager em = jpaApi.em();

        em.persist(this);
    }

    public boolean existsInDb() {
        return userHelper.findById(userId) != null;
    }

}