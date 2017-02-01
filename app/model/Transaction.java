package model;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;

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

    private Transaction(String transactionId, String sourceId, String destId, Currency sourceCurrency, Currency destCurrency, BigDecimal sourceAmount, BigDecimal destAmount, String categoryId, Account sourceAccount, Account destAccount, TransactionCategory category) {
        this.transactionId = transactionId;
        this.sourceId = sourceId;
        this.destId = destId;
        this.sourceCurrency = sourceCurrency;
        this.destCurrency = destCurrency;
        this.sourceAmount = sourceAmount;
        this.destAmount = destAmount;
        this.categoryId = categoryId;
        this.sourceAccount = sourceAccount;
        this.destAccount = destAccount;
        this.category = category;
    }

    public static Transaction createTransaction(String transactionId, String sourceId, String destId, Currency sourceCurrency, Currency destCurrency, BigDecimal sourceAmount, BigDecimal destAmount, String categoryId, Account sourceAccount, Account destAccount, TransactionCategory category) {
        return new Transaction(transactionId, sourceId, destId, sourceCurrency, destCurrency, sourceAmount, destAmount, categoryId, sourceAccount, destAccount, category);
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

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
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

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Account getDestAccount() {
        return destAccount;
    }

    public void setDestAccount(Account destAccount) {
        this.destAccount = destAccount;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }
}