import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import jackson.json.format.Event;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static jackson.json.format.InstantDeserializer.TS_FORMAT;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestEvent {
    private static final Event event = new Event();

    private static final String createdAt = DateTimeFormatter.ofPattern(TS_FORMAT)
            .withZone(ZoneId.from(ZoneOffset.UTC))
            .format(event.getCreatedAt());

    private void test(String result) {
        System.out.println(result);
        assertTrue(result.contains(createdAt));
    }

    @Test
    public void withObjectMapper() throws JsonProcessingException {
        test(new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .writeValueAsString(event));
    }

    @Test
    public void withJSONObject() {
        test(new JSONObject(event).toString());
    }

    @Test
    public void withGson() {
        test(new Gson().toJson(event));
    }
}
