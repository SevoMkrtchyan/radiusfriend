package test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.junit.Test;
import org.radiusfriend.config.SpringConfig;
import org.radiusfriend.controller.request.CreateEventRequest;
import org.radiusfriend.controller.request.SearchEventsRequest;
import org.radiusfriend.dto.Event;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Slf4j
public class CheckSerialization {
    private static final ObjectMapper mapper;

    static {
        mapper = new SpringConfig().objectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    @SneakyThrows
    public void checkCreateEvent() {
        var request = CreateEventRequest.builder()
                .name("eventName")
                .address("address")
                .typeId(2)
                .ownerAge(28)
                .dateTime(new Date()).build();
        String output = mapper.writeValueAsString(request);
        log.info(output);

        output = "{\n" +
                "  \"name\" : \"eventName\",\n" +
                "  \"latitude\" : 30,\n" +
                "  \"longitude\" : -60,\n" +
                "  \"address\" : \"address\",\n" +
                "  \"typeId\" : 2,\n" +
                "  \"ownerAge\" : 28,\n" +
                "  \"dateTime\" : \"2021-07-01T00:00:00+03:00\"\n" +
                "}";
        request = mapper.readValue(output, CreateEventRequest.class);
    }

    @Test
    @SneakyThrows
    public void checkSearch() {
        var request = SearchEventsRequest.builder()
                .userAge(24)
                .endDateTime(new Date())
                .build();
        String output = mapper.writeValueAsString(request);
        log.info(output);
        output = "{\n" +
                "  \"userAge\" : 24,\n" +
                "  \"endDateTime\" : \"2021-07-01T00:00:00+03:00\",\n" +
                "  \"minParticipantCount\" : 0\n" +
                "}";
        request = mapper.readValue(output, SearchEventsRequest.class);
    }

    @Test
    @SneakyThrows
    public void checkEventResponses() {
        var event = Event.builder()
                .dateTime(new Date(Instant.now().plus(4, ChronoUnit.DAYS).toEpochMilli()))
                .created(new Date())
                .build();
        String output = mapper.writeValueAsString(event);
        log.info(output);
        output = "{\n" +
                "  \"id\" : 0,\n" +
                "  \"typeId\" : 0,\n" +
                "  \"ownerAge\" : 0,\n" +
                "  \"dateTime\" : \"2021-07-01T00:00:00+03:00\",\n" +
                "  \"created\" : \"2021-07-01T00:00:00+03:00\"\n" +
                "}";
        event = mapper.readValue(output, Event.class);
    }

    public String solution(List<Integer> numbers) {
     return   numbers.stream().filter(i->i==7).findFirst().map(i-> "Boom").orElse("There is no 7.");
    }

}