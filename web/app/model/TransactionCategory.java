package model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.Objects;

public class TransactionCategory {

    public static final TransactionCategory UNDEFINED = new TransactionCategory(
            "5b0329bfbe9c4b0296f96379",
            "UNDEFINED",
            "Undefined category"
    );

    @MongoObjectId
    private String _id;

    private String name;

    private String description;

    @JsonCreator
    private TransactionCategory(@JsonProperty("_id") String categoryId,
                                @JsonProperty("name") String name,
                                @JsonProperty("description") String description) {
        this._id = categoryId;
        this.name = name;
        this.description = description;
    }

    public TransactionCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static TransactionCategory createTransactionCategory(String categoryId, String name, String description) {
        return new TransactionCategory(categoryId, name, description);
    }

    public static TransactionCategory createTransactionCategory(String name, String description) {
        return new TransactionCategory(name, description);
    }

    public String getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionCategory that = (TransactionCategory) o;

        return Objects.equals(_id, that._id);
    }

    public boolean identicalTo(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionCategory that = (TransactionCategory) o;

        return Objects.equals(_id, that._id)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("categoryId", _id)
                .add("name", name)
                .add("description", description)
                .toString();
    }
}
