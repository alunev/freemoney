package org.alunev.freemoney.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author red
 * @since 0.0.1
 */
public class Sms {

    private String _id;

    private String ownerId;

    private String deviceId;

    private String sender;

    private String text;

    private long createdTs;

    @JsonCreator
    private Sms(@JsonProperty("id") String id,
                @JsonProperty("ownerId") String ownerId,
                @JsonProperty("deviceId") String deviceId,
                @JsonProperty("sender") String sender,
                @JsonProperty("text") String text,
                @JsonProperty("createdTs") long createdTs) {
        this._id = id;
        this.ownerId = ownerId;
        this.deviceId = deviceId;
        this.sender = sender;
        this.text = text;
        this.createdTs = createdTs;
    }

    public static Sms createSms(String id,
                                String ownerId,
                                String deviceId,
                                String sender,
                                String text,
                                long createdTs) {
        return new Sms(id, ownerId, deviceId, sender, text, createdTs);
    }

    public static Sms createSms(String ownerId,
                                String deviceId,
                                String sender,
                                String text,
                                long createdTs) {
        return new Sms(null, ownerId, deviceId, sender, text, createdTs);
    }

    public String getId() {
        return _id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public long getCreatedTs() {
        return createdTs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sms sms = (Sms) o;

        return Objects.equals(_id, sms._id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

    public boolean sameAs(Sms o) {
        if (this == o) return true;
        if (o == null) return false;

        Sms sms = (Sms) o;

        return Objects.equals(_id, sms._id) &&
                Objects.equals(ownerId, sms.ownerId) &&
                Objects.equals(deviceId, sms.deviceId) &&
                Objects.equals(text, sms.text) &&
                Objects.equals(createdTs, sms.createdTs);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("_id", _id)
                .add("ownerId", ownerId)
                .add("deviceId", deviceId)
                .add("sender", sender)
                .add("text", text)
                .add("createdTs", createdTs)
                .toString();
    }
}
