package org.radiusfriend.dao;

import org.radiusfriend.controller.request.CreateEventRequest;
import org.radiusfriend.controller.request.SearchEventsRequest;
import org.radiusfriend.dto.Event;

import java.util.List;

public interface EventDaoService {
    List<Event> findEvents(SearchEventsRequest searchEventsRequest);

    int createEvent(CreateEventRequest request);

    void deactivateAllEvents();
}
