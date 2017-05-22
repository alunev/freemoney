package model;


import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;
import com.google.common.collect.Sets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
@Table(name = "users", schema = "RedisK@redis_pu")
public class User implements Subject {

    public static final User GUEST = User.createUser("", "guest@guest.com");

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "email")
    private String email;

    // relations persistence is handled manually
    @Transient
    private Map<String, Account> accounts = new HashMap<>();

    @Transient
    private Map<String, Transaction> transactions = new HashMap<>();

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
        return createUser(userId, email, Collections.emptySet());
    }

    public static User createUser(String userId, String email, Set<Account> accounts) {
        User user = new User();

        user.userId = userId;
        user.email = email;
        user.accounts = accSetToMap(accounts);

        return user;
    }

    private static Map<String, Account> accSetToMap(Set<Account> accounts) {
        return accounts.stream().collect(Collectors.toMap(Account::getId, Function.identity()));
    }

    private static Map<String, Transaction> txSetToMap(Set<Transaction> accounts) {
        return accounts.stream().collect(Collectors.toMap(Transaction::getTransactionId, Function.identity()));
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public Set<Account> getAccounts() {
        return Sets.newHashSet(accounts.values());
    }

    public void setAccounts(Set<Account> accounts) {
        accounts.forEach(a -> this.accounts.put(a.getId(), a));
    }

    public Optional<Account> getAccountById(String id) {
        if (accounts == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(accounts.get(id));
    }

    public void addAccount(Account account) {
        this.accounts.put(account.getId(), account);
    }

    public void removeAccount(Account account) {
        this.accounts.remove(account.getId());
    }

    public void removeAccountById(String id) {
        this.accounts.remove(id);
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.put(transaction.getTransactionId(), transaction);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction.getTransactionId());
    }

    public Set<Transaction> getTransactions() {
        return Sets.newHashSet(transactions.values());
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = txSetToMap(transactions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return Objects.equals(userId, user.userId);
    }


    public boolean sameAs(User user) {
        if (this == user) return true;
        if (user == null) return false;

        return Objects.equals(userId, user.userId) &&
                Objects.equals(email, user.email) &&
                Objects.equals(accounts, user.accounts) &&
                Objects.equals(transactions, user.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public List<? extends Role> getRoles() {
        return Collections.emptyList();
    }

    @Override
    public List<? extends Permission> getPermissions() {
        return Collections.emptyList();
    }

    @Override
    public String getIdentifier() {
        return userId;
    }
}