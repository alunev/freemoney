package model;


import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

@Entity
@Table(name = "transactions", schema = "RedisK@redis_pu")
public class Transaction {

    @Id
    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "type")
    private TransactionType transactionType;

    @Column(name = "source_id")
    private String sourceId;

    @Column(name = "dest_id")
    private String destId;

    @Column(name = "sourceAmount")
    private BigDecimal sourceAmount;

    @Column(name = "destAmount")
    private BigDecimal destAmount;

    @Column(name = "categoryId")
    private String categoryId;

    @Column(name = "added_date")
    private DateTime addedTime;

    @Transient
    private Account sourceAccount;

    @Transient
    private Account destAccount;

    @Transient
    private TransactionCategory category;

    public Transaction() {
        // required by jpa
    }

    private Transaction(String transactionId,
                        TransactionType transactionType,
                        BigDecimal sourceAmount,
                        BigDecimal destAmount,
                        Account sourceAccount,
                        Account destAccount,
                        TransactionCategory category,
                        DateTime addedTime) {
        checkNotNull(transactionId);
        checkNotNull(transactionType);
        checkNotNull(sourceAccount);
        checkNotNull(destAccount);
        checkNotNull(category);

        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.sourceId = sourceAccount.getId();
        this.destId = destAccount.getId();
        this.sourceAmount = sourceAmount;
        this.destAmount = destAmount;
        this.categoryId = category.getCategoryId();
        this.sourceAccount = sourceAccount;
        this.destAccount = destAccount;
        this.category = category;
        this.addedTime = addedTime;
    }

    public static Transaction createTransfer(String transactionId,
                                             BigDecimal sourceAmount,
                                             BigDecimal destAmount,
                                             Account sourceAccount,
                                             Account destAccount,
                                             TransactionCategory category,
                                             DateTime addedTime) {
        return new Transaction(transactionId, TransactionType.TRANSFER, sourceAmount, destAmount, sourceAccount, destAccount, category, addedTime);
    }

    public static Transaction createExpense(String transactionId,
                                            BigDecimal amount,
                                            Account account,
                                            TransactionCategory category,
                                            DateTime addedTime) {
        return new Transaction(transactionId, TransactionType.EXPENSE, amount, BigDecimal.ZERO, account, Account.EXPENSE_ACCOUNT, category, addedTime);
    }

    public static Transaction createIncome(String transactionId,
                                           BigDecimal amount,
                                           Account account,
                                           TransactionCategory category,
                                           DateTime addedTime) {
        return new Transaction(transactionId, TransactionType.INCOME, amount, BigDecimal.ZERO, account, Account.INCOME_ACCOUNT, category, addedTime);
    }


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getDestId() {
        return destId;
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

    public DateTime getAddedTime() {
        return addedTime;
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