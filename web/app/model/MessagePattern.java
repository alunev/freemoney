package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.time.LocalDateTime;

/**
 * @author red
 * @since 0.0.1
 */
public class MessagePattern {

    @MongoObjectId
    private String _id;

    private String ownerId;

    private String regex;

    private String bankName;

    private LocalDateTime createdTs;

    @JsonCreator
    public MessagePattern(@JsonProperty("id") String _id,
                          @JsonProperty("ownerId") String ownerId,
                          @JsonProperty("regex") String regex,
                          @JsonProperty("bankName") String bankName,
                          @JsonProperty("createdTs") LocalDateTime createdTs) {
        this._id = _id;
        this.ownerId = ownerId;
        this.regex = regex;
        this.bankName = bankName;
        this.createdTs = createdTs;
    }

    public MessagePattern(String ownerId, String regex, String bankName, LocalDateTime createdTs) {
        this.ownerId = ownerId;
        this.regex = regex;
        this.bankName = bankName;
        this.createdTs = createdTs;
    }

    public String getId() {
        return _id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getRegex() {
        return regex;
    }

    public String getBankName() {
        return bankName;
    }

    public LocalDateTime getCreatedTs() {
        return createdTs;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("_id", _id)
                .add("ownerId", ownerId)
                .add("regex", regex)
                .add("bankName", bankName)
                .add("createdTs", createdTs)
                .toString();
    }
}
