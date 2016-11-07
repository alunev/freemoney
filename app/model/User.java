package model;


import play.api.Play;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users", schema = "RedisK@redis_pu")
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "email")
    private String email;

    public User() {
        // required by jpa
    }

    public static User copyOf(User user) {
        User user1 = new User();

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

}