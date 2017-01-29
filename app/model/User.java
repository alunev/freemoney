package model;


import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users", schema = "RedisK@redis_pu")
public class User {

    public static final User GUEST = User.createUser("", "guest@guest.com");

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "email")
    private String email;

    // relations persistence is handled manually
    @Transient
    private List<Account> accounts = new ArrayList<>();

    public User() {
        // required by jpa
    }

    public static User copyOf(User user) {
        return createUser(user.getUserId(), user.getEmail(), user.getAccounts());
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

    public String getEmail() {
        return email;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Optional<Account> getAccountById(String id) {
        if (accounts == null) {
            return Optional.empty();
        }

        return accounts.stream().filter(account -> account.getId().equals(id)).findFirst();
    }

    public void addAccount(Account account) {
        ArrayList<Account> accounts = Lists.newArrayList(this.accounts);
        accounts.add(account);

        this.accounts = accounts;
    }

    public void removeAccount(Account account) {
        this.accounts.remove(account);
    }

    public void removeAccountById(String id) {
        this.accounts = this.accounts.stream().filter(account -> !account.getId().equals(id)).collect(Collectors.toList());
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