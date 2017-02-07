package model;


import com.google.common.base.Objects;
import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
@Table(name = "accounts", schema = "RedisK@redis_pu")
@IndexCollection(columns={@Index(name="ownerId"),@Index(name="currency")})
public class Account {
    @Id
    @Column(name = "account_id")
    private String id;

    @Column(name = "ownerId")
    private String ownerId;

    @Column(name = "number")
    private String number;

    @Column(name = "title")
    private String title;

    @Column(name = "currency")
    private Currency currency;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "pattern")
    private String inPattern;

    public Account() {
        
    }

    public static Account createAccount(String id, String ownerId, String number, String title, Currency currency,
                                        BigDecimal balance, String inPattern) {
        Account account = new Account();
        
        account.id = id;
        account.ownerId = ownerId;
        account.number = number;
        account.title = title;
        account.currency = currency;
        account.balance = balance;
        account.inPattern = inPattern;

        return account;
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getInPattern() {
        return inPattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equal(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public boolean isSame(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Account account = (Account) o;

        return Objects.equal(id, account.id) &&
                Objects.equal(number, account.number) &&
                Objects.equal(title, account.title) &&
                Objects.equal(currency, account.currency) &&
                Objects.equal(balance, account.balance) &&
                Objects.equal(inPattern, account.inPattern);
    }
}
