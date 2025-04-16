package jackson.json.format;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class InstantDeserializer extends JsonDeserializer<Instant> {

    public static final String TS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss[.SSS][XXXXX]";

    @Override
    public Instant deserialize(final JsonParser parser,
                               final DeserializationContext context) throws IOException, JsonProcessingException {
        if (isNull(parser)) {
            String message = "Invalid input " + JsonParser.class.getSimpleName() + " argument";
            System.out.println(message);
            throw new IllegalArgumentException(message);
        }
        return deserialize(parser.getText());
    }

    public static Instant deserialize(final String text) throws IllegalArgumentException {

        if (isNull(text) || text.isEmpty()) {
            String message = "Invalid input " + String.class.getSimpleName() + " text argument";
            System.out.println(message);
            throw new IllegalArgumentException(message);
        }

        String tsFormat = TS_FORMAT;
        Matcher msecMatcher = Pattern.compile(".*[.](\\d+)")
                                     .matcher(text);
        if (msecMatcher.find()
         && msecMatcher.groupCount() == 1) {
            tsFormat = tsFormat.replace("[.SSS]",
                                        "[." + "S".repeat(msecMatcher.group(1).length()) + "]");
        }

        return OffsetDateTime.parse(text,
                                    new DateTimeFormatterBuilder()
                                            .appendPattern(tsFormat)
                                            .optionalStart()
                                                .appendOffset("+HHmmss", "Z")
                                            .optionalEnd()
                                            .toFormatter())
                             .toInstant();
    }
}
