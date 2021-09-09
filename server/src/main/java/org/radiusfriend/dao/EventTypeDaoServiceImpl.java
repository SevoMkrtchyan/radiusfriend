package org.radiusfriend.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.radiusfriend.dto.EventTypeDto;
import org.radiusfriend.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventTypeDaoServiceImpl implements EventTypeDaoService {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public int saveEventType(String eventName) {
        AtomicInteger rowId = new AtomicInteger();
        String sql = "INSERT INTO event_types (name) values (?) ON CONFLICT (name) DO UPDATE SET name=EXCLUDED.name RETURNING id";
        jdbcTemplate.query(sql, prSt -> {
            prSt.setString(1, eventName);
        }, rs -> {
            rowId.set(rs.getInt("id"));
        });
        return rowId.get();
    }

    @Override
    @Transactional
    public void incTypeCounter(int typeId) {
        String sql = "UPDATE event_types SET count=count+1 WHERE id=?";
        val result = jdbcTemplate.update(sql, typeId);
        if (result == 0) {
            throw new DataNotFoundException();
        }
    }

    @Override
    public EventTypeDto getEventTypeById(int eventTypeId) {
        try {
            return jdbcTemplate.query("SELECT id, name FROM event_types WHERE id = ?", new Object[]{eventTypeId},
                    resultSet -> {
                        if (!resultSet.next()) {
                            throw new DataNotFoundException();
                        }
                        return EventTypeDto.builder()
                                .id(resultSet.getInt("id"))
                                .name(resultSet.getString("name"))
                                .build();
                    });
        } catch (Throwable t) {
            log.error("Database error", t);
            throw new DataNotFoundException();
        }
    }

    @Override
    public List<EventTypeDto> getTypesByRate(String name, int requestLimit) {
        List<EventTypeDto> eventTypeNames = new ArrayList<>();
        jdbcTemplate.query("SELECT name, id FROM event_types " +
                        "WHERE name ILIKE ? " +
                        "ORDER BY count DESC LIMIT ?",
                new Object[]{"%" + name + "%", requestLimit}, resultSet -> {
                    if (resultSet.isAfterLast()) {
                        return;
                    }
                    do {
                        eventTypeNames.add(EventTypeDto.builder()
                                .name(resultSet.getString("name"))
                                .id(resultSet.getInt("id"))
                                .build());
                    } while (resultSet.next());
                });
        return eventTypeNames;
    }
}