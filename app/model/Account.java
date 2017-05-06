package model;


import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;
import dao.RedisNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
@Table(name = "accounts", schema = "RedisK@redis_pu")
@IndexCollection(columns={@Index(name="ownerId"),@Index(name="currency")})
public class Account {

    public static final Account INCOME_ACCOUNT = createAccount(
            "e64fd32d-b979-44e6-951a-7eb596df229c",
            RedisNull.NULL,
            RedisNull.NULL,
            RedisNull.NULL,
            Currency.getInstance("USD"),
            BigDecimal.ZERO,
            RedisNull.NULL
    );

    public static final Account EXPENSE_ACCOUNT = createAccount(
            "644214a5-43da-4470-8cf6-3f303687e45c",
            RedisNull.NULL,
            RedisNull.NULL,
            RedisNull.NULL,
            Currency.getInstance("USD"),
            BigDecimal.ZERO,
            RedisNull.NULL
    );

    @Id
    @Column(name = "account_id")
    @SequenceGenerator(name = "seq_gen", allocationSize = 20, initialValue = 80)
    @GeneratedValue(generator = "seq_gen", strategy = GenerationType.SEQUENCE)
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
    private String smsPattern;

    public Account() {

    }

    public static Account createAccount(String id, String ownerId, String number, String title, Currency currency,
                                        BigDecimal balance, String smsPattern) {
        Account account = new Account();

        account.id = id;
        account.ownerId = ownerId;
        account.number = number;
        account.title = title;
        account.currency = currency;
        account.balance = balance;
        account.smsPattern = smsPattern;

        return account;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getSmsPattern() {
        return smsPattern;
    }

    public void setSmsPattern(String smsPattern) {
        this.smsPattern = smsPattern;
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

        return Objects.equal(id, account.id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("ownerId", ownerId)
                .add("number", number)
                .add("title", title)
                .add("currency", currency)
                .add("balance", balance)
                .add("smsPattern", smsPattern)
                .toString();
    }
}
