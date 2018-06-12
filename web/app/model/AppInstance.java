package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * @author red
 * @since 0.0.1
 */
public class AppInstance {
    private final String instanceId;

    private final ZonedDateTime lastSync;

    public AppInstance(@JsonProperty("instanceId") String instanceId, @JsonProperty("lastSync") ZonedDateTime lastSync) {
        this.instanceId = instanceId;
        this.lastSync = lastSync;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public ZonedDateTime getLastSync() {
        return lastSync;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppInstance that = (AppInstance) o;

        return Objects.equals(instanceId, that.instanceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("instanceId", instanceId)
                .add("lastSync", lastSync)
                .toString();
    }
}
