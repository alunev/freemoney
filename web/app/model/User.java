package model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class User {

    public static final User GUEST = User.createEmptyUser("5a4ec3a6b7bae729a6d9a18a", "", "guest@guest.com");

    @MongoObjectId
    private String _id;

    private final String authId;

    private final String email;

    @JsonIgnore
    private final Set<Account> accounts;

    @JsonIgnore
    private final Set<Transaction> transactions;

    @JsonCreator
    public User(@JsonProperty("_id") String _id,
                @JsonProperty("authId") String authId,
                @JsonProperty("email")String email,
                @JsonProperty("accounts") Set<Account> accounts,
                @JsonProperty("transactions") Set<Transaction> transactions) {
        this._id = _id;
        this.authId = authId;
        this.email = email;
        this.accounts = accounts;
        this.transactions = transactions;
    }

    public User(String authId,
                String email,
                Set<Account> accounts,
                Set<Transaction> transactions) {
        this.authId = authId;
        this.email = email;
        this.accounts = accounts;
        this.transactions = transactions;
    }


    public static User createEmptyUser(String email) {
        return createEmptyUser("", email);
    }

    public static User createEmptyUser(String id, String authId, String email) {
        return new UserBuilder().withAuthId(authId)
                .with_id(id)
                .withEmail(email)
                .withAccounts(Collections.emptySet())
                .withTransactions(Collections.emptySet())
                .build();
    }

    public static User createEmptyUser(String authId, String email) {
        return new UserBuilder().withAuthId(authId)
                                .withEmail(email)
                                .withAccounts(Collections.emptySet())
                                .withTransactions(Collections.emptySet())
                                .build();
    }

    public static User createUserWithAccounts(String email, Set<Account> accounts) {
        return new UserBuilder().withEmail(email)
                                .withAccounts(accounts)
                                .withTransactions(Collections.emptySet())
                                .build();
    }

    public static User createUserWithAccounts(String authId, String email, Set<Account> accounts) {
        return new UserBuilder().withAuthId(authId)
                                .withEmail(email)
                                .withAccounts(accounts)
                                .withTransactions(Collections.emptySet())
                                .build();
    }

    public static User createUser(String authId, String email, Set<Account> accounts, Set<Transaction> transactions) {
        return new UserBuilder().withAuthId(authId)
                                .withEmail(email)
                                .withAccounts(accounts)
                                .withTransactions(Collections.emptySet())
                                .build();
    }

    public static User createUser(String id, String authId, String email, Set<Account> accounts, Set<Transaction> transactions) {
        return new UserBuilder().with_id(id)
                                .withAuthId(authId)
                                .withEmail(email)
                                .withAccounts(accounts)
                                .withTransactions(Collections.emptySet())
                                .build();
    }

    public String getId() {
        return _id;
    }

    public String getAuthId() {
        return authId;
    }

    public String getEmail() {
        return email;
    }

    public Set<Account> getAccounts() {
        return accounts == null ? Collections.emptySet() : Sets.newHashSet(accounts);
    }

    public Set<Transaction> getTransactions() {
        return transactions == null ? Collections.emptySet() : Sets.newHashSet(transactions);
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
               Objects.equals(authId, user.authId) &&
               Objects.equals(email, user.email) &&
               Objects.equals(accounts, user.accounts) &&
               Objects.equals(transactions, user.transactions);
    }

    public boolean isGuest() {
        return this.equals(GUEST);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("_id", _id)
                          .add("authId", authId)
                          .add("email", email)
                          .add("accounts", accounts)
                          .add("transactions", transactions)
                          .toString();
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static UserBuilder builder(User user) {
        return new UserBuilder(user);
    }

}