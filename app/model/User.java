package model;


import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.Sets;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class User implements Subject {

    public static final User GUEST = User.createEmptyUser("", "guest@guest.com");

    @MongoId // auto
    @MongoObjectId
    private final String id;

    private final String email;

    private final Set<Account> accounts;

    private final Set<Transaction> transactions;

    @JsonCreator
    public User(String id, String email, Set<Account> accounts, Set<Transaction> transactions) {
        this.id = id;
        this.email = email;
        this.accounts = accounts;
        this.transactions = transactions;
    }

    public static User createEmptyUser(String userId, String email) {
        return new User(userId, email, Collections.emptySet(), Collections.emptySet());
    }

    public String getId() {
        return id;
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
        this.accounts.add(account);
    }

    public void removeAccount(Account account) {
        this.accounts.remove(account);
    }

    public void addTransaction(Transaction transaction) {
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

        return Objects.equals(id, user.id);
    }


    public boolean sameAs(User user) {
        if (this == user) return true;
        if (user == null) return false;

        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(accounts, user.accounts) &&
                Objects.equals(transactions, user.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
        return id;
    }
}