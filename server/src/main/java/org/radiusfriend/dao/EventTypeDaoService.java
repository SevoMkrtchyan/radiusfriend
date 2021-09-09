package org.radiusfriend.dao;

import org.radiusfriend.dto.EventTypeDto;

import java.util.List;

public interface EventTypeDaoService {

    int saveEventType(String eventName);

    void incTypeCounter(int typeId);

    EventTypeDto getEventTypeById(int eventTypeId);

    List<EventTypeDto> getTypesByRate(String name, int requestLimit);
}
