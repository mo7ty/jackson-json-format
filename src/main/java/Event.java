import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public class Event {

    public static final String TS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TS_FORMAT, timezone = "UTC")
    private Instant createdAt = Instant.now();

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
