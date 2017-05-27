package model;


import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.Sets;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class User implements Subject {

    public static final User GUEST = User.createEmptyUser("", "guest@guest.com");

    @MongoObjectId
    private String _id;

    private final String email;

    private final Set<Account> accounts;

    private final Set<Transaction> transactions;

    @JsonCreator
    public User(String email, Set<Account> accounts, Set<Transaction> transactions) {
        this.email = email;
        this.accounts = accounts;
        this.transactions = transactions;
    }

    public static User createEmptyUser(String email) {
        return new User(email, Collections.emptySet(), Collections.emptySet());
    }

    public static User createEmptyUser(String userId, String email) {
        return new User(email, Collections.emptySet(), Collections.emptySet());
    }

    public static User createUserWithAccounts(String userId, String email, Set<Account> accounts) {
        return new User(email, accounts, Collections.emptySet());
    }

    public String getId() {
        return _id;
    }

    public String getEmail() {
        return email;
    }

    public Set<Account> getAccounts() {
        return Sets.newHashSet(accounts);
    }

    public Set<Transaction> getTransactions() {
        return Sets.newHashSet(transactions);
    }

    public void addAccount(Account account) {
        if (accounts.contains(account)) {
            throw new IllegalArgumentException("Duplicate account _id: " + account);
        }

        this.accounts.add(account);
    }

    public void removeAccount(Account account) {
        this.accounts.remove(account);
    }

    public void addTransaction(Transaction transaction) {
        if (transactions.contains(transaction)) {
            throw new IllegalArgumentException("Duplicate transaction _id: " + transaction);
        }

        this.transactions.add(transaction);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return Objects.equals(_id, user._id);
    }


    public boolean sameAs(User user) {
        if (this == user) return true;
        if (user == null) return false;

        return Objects.equals(_id, user._id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(accounts, user.accounts) &&
                Objects.equals(transactions, user.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
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
        return _id;
    }
}