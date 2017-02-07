package model;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

@Entity
@Table(name = "transactions", schema = "RedisK@redis_pu")
public class Transaction {

    @Id
    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "source_id")
    private String sourceId;

    @Column(name = "dest_id")
    private String destId;

    @Column(name = "source_currency")
    private Currency sourceCurrency;

    @Column(name = "dest_currency")
    private Currency destCurrency;

    @Column(name = "sourceAmount")
    private BigDecimal sourceAmount;

    @Column(name = "destAmount")
    private BigDecimal destAmount;

    @Column(name = "categoryId")
    private String categoryId;

    @Transient
    private Account sourceAccount;

    @Transient
    private Account destAccount;

    @Transient
    private TransactionCategory category;

    public Transaction() {
        // required by jpa
    }

    private Transaction(String transactionId, Currency sourceCurrency, Currency destCurrency, BigDecimal sourceAmount, BigDecimal destAmount, Account sourceAccount, Account destAccount, TransactionCategory category) {
        checkNotNull(sourceAccount);
        checkNotNull(destAccount);
        checkNotNull(category);

        this.transactionId = transactionId;
        this.sourceId = sourceAccount.getId();
        this.destId = destAccount.getId();
        this.sourceCurrency = sourceCurrency;
        this.destCurrency = destCurrency;
        this.sourceAmount = sourceAmount;
        this.destAmount = destAmount;
        this.categoryId = category.getCategoryId();
        this.sourceAccount = sourceAccount;
        this.destAccount = destAccount;
        this.category = category;
    }

    public static Transaction createTransaction(String transactionId, String sourceId, String destId, Currency sourceCurrency, Currency destCurrency, BigDecimal sourceAmount, BigDecimal destAmount, String categoryId, Account sourceAccount, Account destAccount, TransactionCategory category) {
        return new Transaction(transactionId, sourceCurrency, destCurrency, sourceAmount, destAmount, sourceAccount, destAccount, category);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getDestId() {
        return destId;
    }

    public Currency getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(Currency sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public Currency getDestCurrency() {
        return destCurrency;
    }

    public void setDestCurrency(Currency destCurrency) {
        this.destCurrency = destCurrency;
    }

    public BigDecimal getSourceAmount() {
        return sourceAmount;
    }

    public void setSourceAmount(BigDecimal sourceAmount) {
        this.sourceAmount = sourceAmount;
    }

    public BigDecimal getDestAmount() {
        return destAmount;
    }

    public void setDestAmount(BigDecimal destAmount) {
        this.destAmount = destAmount;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceId = sourceAccount.getId();
        this.sourceAccount = sourceAccount;
    }

    public Account getDestAccount() {
        return destAccount;
    }

    public void setDestAccount(Account destAccount) {
        this.destId = destAccount.getId();
        this.destAccount = destAccount;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
        this.categoryId = category.getCategoryId();
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}