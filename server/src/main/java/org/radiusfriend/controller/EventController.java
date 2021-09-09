package org.radiusfriend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.radiusfriend.controller.request.CreateEventRequest;
import org.radiusfriend.controller.request.SearchEventsRequest;
import org.radiusfriend.dto.Event;
import org.radiusfriend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.radiusfriend.consts.WebConsts.EVENTS;

@RestController
@RequestMapping(value = EVENTS, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Tag(name = "Контроллер событий", description = "Управление событиями")
public class EventController {
    private final EventService eventService;

    @PostMapping("find")
    @Operation(summary = "Поиск событий",
            description = "Позволяет искать события по заданным критериям")
    public ResponseEntity<List<Event>> findEvents(@Valid @RequestBody
                                                  @Parameter(description = "Запрос на поиск событий", required = true)
                                                          SearchEventsRequest request) {
        log.debug("Search events request={}", request);
        val events = eventService.findEvents(request);
        log.debug("Found events={}", events);
        return ResponseEntity.ok(events);
    }

    @PostMapping("create")
    @Operation(summary = "Создание события",
            description = "Позволяет создать событие")
    public ResponseEntity<Integer> createEvent(@Valid @RequestBody
                                               @Parameter(description = "Запрос на создание события", required = true)
                                                       CreateEventRequest request) {
        log.debug("Create event request {}", request);
        int eventId = eventService.createEvent(request);
        log.debug("Event created. EventId={}", eventId);
        return ResponseEntity.ok(eventId);
    }
}