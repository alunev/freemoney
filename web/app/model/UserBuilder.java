package model;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class UserBuilder {
    private String id;
    private String authId;
    private String email;
    private List<AppInstance> appInstances;
    private Set<Account> accounts = Collections.emptySet();
    private Set<Transaction> transactions = Collections.emptySet();

    public UserBuilder() {

    }

    public UserBuilder(User user) {
        this.id = user.get_id();
        this.authId = user.getAuthId();
        this.email = user.getEmail();
        this.appInstances = user.getAppInstances();
        this.accounts = user.getAccounts();
        this.transactions = user.getTransactions();
    }

    public UserBuilder with_id(String id) {
        this.id = id;
        return this;
    }

    public UserBuilder withAuthId(String authId) {
        this.authId = authId;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withAppInstances(List<AppInstance> instances) {
        this.appInstances = instances;
        return this;
    }

    public UserBuilder withAccounts(Set<Account> accounts) {
        this.accounts = Sets.newHashSet(accounts);
        return this;
    }

    public UserBuilder withTransactions(Set<Transaction> transactions) {
        this.transactions = Sets.newHashSet(transactions);
        return this;
    }

    public User build() {
        return new User(id, authId, email, appInstances, accounts, transactions);
    }
}