package model;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * @author red
 * @since 0.0.1
 */
@Entity
@Table(name = "smses", schema = "RedisK@redis_pu")
@IndexCollection(columns={@Index(name="ownerId")})
public class Sms {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "ownerId")
    private String ownerId;

    @Column(name = "deviceId")
    private String deviceId;

    @Column(name = "text")
    private String text;

    @Column(name = "createdTs")
    private LocalDateTime createdTs;

    public Sms() {
        // for jpa
    }

    private Sms(String id, String ownerId, String deviceId, String text, LocalDateTime createdTs) {
        this.id = id;
        this.ownerId = ownerId;
        this.deviceId = deviceId;
        this.text = text;
        this.createdTs = createdTs;
    }

    public static Sms createSms(String id, String ownerId, String deviceId, String text, LocalDateTime createdTs) {
        ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime();
        return new Sms(id, ownerId, deviceId, text, createdTs);
    }

    public static Sms createSms(String ownerId, String deviceId, String text, LocalDateTime createdTs) {
        return new Sms("", ownerId, deviceId, text, createdTs);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedTs() {
        return createdTs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sms sms = (Sms) o;
        return Objects.equals(id, sms.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean sameAs(Sms o) {
        if (this == o) return true;
        if (o == null) return false;

        Sms sms = (Sms) o;

        return Objects.equals(id, sms.id) &&
                Objects.equals(ownerId, sms.ownerId) &&
                Objects.equals(deviceId, sms.deviceId) &&
                Objects.equals(text, sms.text) &&
                Objects.equals(createdTs, sms.createdTs);
    }

}