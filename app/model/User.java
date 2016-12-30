package model;


import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "RedisK@redis_pu")
public class User {

    public static final User GUEST = User.createUser("", "guest@guest.com");

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "email")
    private String email;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<Account> accounts;

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
        return createUser(userId, email, Collections.emptyList());
    }

    public static User createUser(String userId, String email, List<Account> accounts) {
        User user = new User();

        user.userId = userId;
        user.email = email;
        user.accounts = Lists.newArrayList(accounts);

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

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}