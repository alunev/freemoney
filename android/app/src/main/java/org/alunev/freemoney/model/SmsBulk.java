package org.alunev.freemoney.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * @author red
 * @since 0.0.1
 */
public class SmsBulk {
    private final String userId;

    private final String deviceId;

    private final long createdTs;

    private final List<Sms> list;

    @JsonCreator
    public SmsBulk(@JsonProperty("userId") String userId,
                   @JsonProperty("deviceId") String deviceId,
                   @JsonProperty("createdTs") long createdTs,
                   @JsonProperty("list") List<Sms> list) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.createdTs = createdTs;
        this.list = list;
    }

    public String getUserId() {
        return userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public long getCreatedTs() {
        return createdTs;
    }

    public List<Sms> getList() {
        return list;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("userId", userId)
                .add("deviceId", deviceId)
                .add("createdTs", createdTs)
                .add("list", list)
                .toString();
    }
}
