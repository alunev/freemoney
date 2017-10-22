package model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import dao.RedisNull;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.math.BigDecimal;
import java.util.Currency;

public class Account {

    public static final Account INCOME_ACCOUNT = new Account("507f191e810c19729de860ea",
                                                             RedisNull.NULL,
                                                             RedisNull.NULL,
                                                             RedisNull.NULL,
                                                             Currency.getInstance("USD"),
                                                             BigDecimal.ZERO,
                                                             RedisNull.NULL
    );

    public static final Account EXPENSE_ACCOUNT = createAccount("507f191e810c19729de860ea",
                                                                RedisNull.NULL,
                                                                RedisNull.NULL,
                                                                Currency.getInstance("USD"),
                                                                BigDecimal.ZERO,
                                                                RedisNull.NULL
    );

    @MongoObjectId
    private String _id;

    private final String ownerId;

    private final String number;

    private final String title;

    private final Currency currency;

    private final BigDecimal balance;

    private final String smsPattern;

    @JsonCreator
    public Account(@JsonProperty("_id") String _id,
                   @JsonProperty("ownerId") String ownerId,
                   @JsonProperty("number") String number,
                   @JsonProperty("title") String title,
                   @JsonProperty("currency") Currency currency,
                   @JsonProperty("balance") BigDecimal balance,
                   @JsonProperty("smsPattern") String smsPattern) {
        this._id = _id;
        this.ownerId = ownerId;
        this.number = number;
        this.title = title;
        this.currency = currency;
        this.balance = balance;
        this.smsPattern = smsPattern;
    }

    public Account(String ownerId,
                   String number,
                   String title,
                   Currency currency,
                   BigDecimal balance,
                   String smsPattern) {
        this.ownerId = ownerId;
        this.number = number;
        this.title = title;
        this.currency = currency;
        this.balance = balance;
        this.smsPattern = smsPattern;
    }

    public static Account createAccount(String ownerId,
                                        String number,
                                        String title,
                                        Currency currency,
                                        BigDecimal balance,
                                        String smsPattern) {
        return new Account(ownerId, number, title, currency, balance, smsPattern);
    }

    public static Account copyWithOwnerId(Account a, String ownerId) {
        return new Account(ownerId, a.getNumber(), a.getTitle(), a.getCurrency(), a.getBalance(), a.getSmsPattern());
    }

    public String getId() {
        return _id;
    }

    public String getOwnerId() {
        return ownerId;
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

    public String getSmsPattern() {
        return smsPattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return Objects.equal(_id, account._id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(_id);
    }

    public boolean isSame(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Account account = (Account) o;

        return Objects.equal(_id, account._id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("_id", _id)
                          .add("ownerId", ownerId)
                          .add("number", number)
                          .add("title", title)
                          .add("currency", currency)
                          .add("balance", balance)
                          .add("smsPattern", smsPattern)
                          .toString();
    }
}
