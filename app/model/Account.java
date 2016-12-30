package model;


import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
@Table(name = "accounts", schema = "RedisK@redis_pu")
public class Account {
    @Id
    @Column(name = "account_id")
    private String id;

    @Column(name = "number")
    private String number;

//    @Column(name = "title")
//    private String title;
//
//    @Column(name = "currency")
//    private Currency currency;
//
//    @Column(name = "balance")
//    private BigDecimal balance;
//
//    @Column(name = "pattern")
//    private String inPattern;

    public Account() {
        
    }

    public static Account createAccount(String id, String number, String title, Currency currency, BigDecimal balance, String inPattern) {
        Account account = new Account();
        
        account.id = id;
        account.number = number;
//        account.title = title;
//        account.currency = currency;
//        account.balance = balance;
//        account.inPattern = inPattern;

        return account;
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
}
