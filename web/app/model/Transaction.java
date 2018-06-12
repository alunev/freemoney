package model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import lombok.Builder;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class Transaction {

    @MongoObjectId
    private String _id;

    private String ownerId;

    private TransactionType transactionType;

    private String categoryId;

    private String sourceId;

    private String destId;

    private BigDecimal sourceAmount;

    private BigDecimal destAmount;

    private LocalDateTime addedTime;

    private TransactionCategory category;

    private Account sourceAccount;

    private Account destAccount;

    public Transaction() {
        // required by jpa
    }

    @JsonCreator
    public Transaction(@JsonProperty("id") String id,
                       @JsonProperty("ownerId") String ownerId,
                       @JsonProperty("transactionType") TransactionType transactionType,
                       @JsonProperty("sourceAmount") BigDecimal sourceAmount,
                       @JsonProperty("destAmount") BigDecimal destAmount,
                       @JsonProperty("sourceId") String sourceId,
                       @JsonProperty("destId") String destId,
                       @JsonProperty("categoryId") String categoryId,
                       @JsonProperty("addedTime") LocalDateTime addedTime) {
        checkNotNull(id);
        checkNotNull(ownerId);
        checkNotNull(transactionType);
        checkNotNull(sourceId);
        checkNotNull(destId);
        checkNotNull(categoryId);
        checkNotNull(addedTime);

        this._id = id;
        this.ownerId = ownerId;
        this.transactionType = transactionType;
        this.sourceId = sourceId;
        this.destId = destId;
        this.sourceAmount = sourceAmount;
        this.destAmount = destAmount;
        this.categoryId = categoryId;
        this.addedTime = addedTime;
    }

    @Builder
    private Transaction(String ownerId,
                        TransactionType transactionType,
                        BigDecimal sourceAmount,
                        BigDecimal destAmount,
                        String sourceId,
                        String destId,
                        String categoryId,
                        LocalDateTime addedTime) {
        checkNotNull(ownerId);
        checkNotNull(transactionType);
        checkNotNull(sourceId);
        checkNotNull(destId);
        checkNotNull(categoryId);

        this.ownerId = ownerId;
        this.transactionType = transactionType;
        this.sourceId = sourceId;
        this.destId = destId;
        this.sourceAmount = sourceAmount;
        this.destAmount = destAmount;
        this.categoryId = categoryId;
        this.addedTime = addedTime;
    }

    public static Transaction createTransfer(String ownerId,
                                             BigDecimal sourceAmount,
                                             BigDecimal destAmount,
                                             Account sourceAccount,
                                             Account destAccount,
                                             TransactionCategory category,
                                             LocalDateTime addedTime) {
        return new Transaction(
                ownerId,
                TransactionType.TRANSFER,
                sourceAmount,
                destAmount,
                sourceAccount.getId(),
                destAccount.getId(),
                category.getId(),
                addedTime);
    }

    public static Transaction createTransfer(String transactionId,
                                             String ownerId,
                                             BigDecimal sourceAmount,
                                             BigDecimal destAmount,
                                             Account sourceAccount,
                                             Account destAccount,
                                             TransactionCategory category,
                                             LocalDateTime addedTime) {
        return new Transaction(
                transactionId,
                ownerId,
                TransactionType.TRANSFER,
                sourceAmount,
                destAmount,
                sourceAccount.getId(),
                destAccount.getId(),
                category.getId(),
                addedTime
        );
    }

    public static Transaction createExpense(String ownerId,
                                            BigDecimal amount,
                                            Account account,
                                            TransactionCategory category,
                                            LocalDateTime addedTime) {
        return new Transaction(
                ownerId,
                TransactionType.EXPENSE,
                amount,
                BigDecimal.ZERO,
                account.getId(),
                Account.EXPENSE_ACCOUNT.getId(),
                category.getId(),
                addedTime
        );
    }

    public static Transaction createIncome(String ownerId,
                                           BigDecimal amount,
                                           Account account,
                                           TransactionCategory category,
                                           LocalDateTime addedTime) {
        return new Transaction(
                ownerId,
                TransactionType.INCOME,
                amount,
                BigDecimal.ZERO,
                account.getId(),
                Account.INCOME_ACCOUNT.getId(),
                category.getId(),
                addedTime);
    }

    public static Transaction copyWithOwnerId(Transaction t, String ownerId) {
        throw new RuntimeException("copyWithOwnerId()");
    }

    public String getId() {
        return _id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setId(String id) {
        this._id = id;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public void setDestAccount(Account destAccount) {
        this.destAccount = destAccount;
    }

    public LocalDateTime getAddedTime() {
        return addedTime;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setAddedTime(LocalDateTime addedTime) {
        this.addedTime = addedTime;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isTransfer() {
        return transactionType == TransactionType.TRANSFER;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(_id, that._id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("_id", _id)
                .add("ownerId", ownerId)
                .add("transactionType", transactionType)
                .add("categoryId", categoryId)
                .add("sourceId", sourceId)
                .add("destId", destId)
                .add("sourceAmount", sourceAmount)
                .add("destAmount", destAmount)
                .add("addedTime", addedTime)
                .add("category", category)
                .add("sourceAccount", sourceAccount)
                .add("destAccount", destAccount)
                .toString();
    }
}