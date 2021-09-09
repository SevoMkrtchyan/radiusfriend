package org.radiusfriend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.radiusfriend.dao.EventDaoService;
import org.radiusfriend.dao.EventTypeDaoService;
import org.radiusfriend.controller.request.CreateEventRequest;
import org.radiusfriend.controller.request.SearchEventsRequest;
import org.radiusfriend.dto.Event;
import org.radiusfriend.dto.EventTypeDto;
import org.radiusfriend.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventService {
    private final EventDaoService eventDaoService;
    private final EventTypeDaoService eventTypeDaoService;

    public List<Event> findEvents(SearchEventsRequest request) {
        log.debug("Request to find events {}", request);
        val now = new Date();
        if (request.getStartDateTime() == null || request.getStartDateTime().before(now)) {
            request.setStartDateTime(now);
        }
        if (request.getEndDateTime() != null &&
                request.getStartDateTime().after(request.getEndDateTime())) {
            throw new ValidationException("EndDateTime должно быть позже StartDateTime");
        }
        if (request.getMaxParticipantCount() != null &&
                request.getMinParticipantCount() > request.getMaxParticipantCount()) {
            throw new ValidationException("MinParticipantCount должен быть меньше MaxParticipantCount");
        }
        if (!((request.getCentreLatitude() == null && request.getCentreLongitude() == null && request.getRadius() == null) ||
                (request.getCentreLatitude() != null && request.getCentreLongitude() != null && request.getRadius() != null))) {
            throw new ValidationException("Longitude, latitude and radius should be specified or be null");
        }
        val events = eventDaoService.findEvents(request);
        events.forEach(event -> {
            EventTypeDto typeDto = eventTypeDaoService.getEventTypeById(event.getTypeId());
            event.setName(typeDto.getName());
        });
        log.debug("Events found = {}", events);
        return events;
    }

    public int createEvent(CreateEventRequest request) {
        log.debug("Request to create event {}", request);
        if (request.getDateTime().before(new Date())) {
            throw new ValidationException("Дата начала события не может быть в прошлом");
        }
        if (request.getMinParticipantCount() != null && request.getMaxParticipantCount() != null &&
                request.getMinParticipantCount() > request.getMaxParticipantCount()) {
            throw new ValidationException("fromParticipantCount должен быть меньше toParticipantCount");
        }
        if (request.getMinAgeOfParticipants() != null && request.getMaxAgeOfParticipants() != null &&
                request.getMinAgeOfParticipants() > request.getMaxAgeOfParticipants()) {
            throw new ValidationException("MinAge должен быть меньше MaxAge");
        }
        int typeId = eventTypeDaoService.saveEventType(request.getName());
        eventTypeDaoService.incTypeCounter(typeId);
        request.setTypeId(typeId);
        val eventId = eventDaoService.createEvent(request);
        log.debug("Event created. EventId={}", eventId);
        return eventId;
    }

    public void deactivateAllEvents() {
        log.info("Request to delete events");
        eventDaoService.deactivateAllEvents();
        log.info("Events deleted");
    }
}