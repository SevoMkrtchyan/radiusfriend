package org.radiusfriend.service;

import org.radiusfriend.dao.EventTypeDaoService;
import org.radiusfriend.dto.EventTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventTypeService {
    private final EventTypeDaoService eventTypeDaoService;
    private final int requestLimit;

    @Autowired
    public EventTypeService(EventTypeDaoService eventTypeDaoService, @Value("${eventtypes.requestlimit:3}") int requestLimit) {
        this.eventTypeDaoService = eventTypeDaoService;
        this.requestLimit = requestLimit;
    }

    public int saveEventType(String eventName) {
        return eventTypeDaoService.saveEventType(eventName);
    }

    public void incTypeCounter(int typeId) {
        eventTypeDaoService.incTypeCounter(typeId);
    }

    public EventTypeDto getEventTypeById(int eventTypeId) {
        return eventTypeDaoService.getEventTypeById(eventTypeId);
    }

    public List<EventTypeDto> getTypesByRate(String name) {
        return eventTypeDaoService.getTypesByRate(name, requestLimit);
    }
}