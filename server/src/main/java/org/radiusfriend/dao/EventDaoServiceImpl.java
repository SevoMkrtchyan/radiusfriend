package org.radiusfriend.dao;

import lombok.RequiredArgsConstructor;
import org.radiusfriend.controller.request.CreateEventRequest;
import org.radiusfriend.controller.request.SearchEventsRequest;
import org.radiusfriend.dto.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventDaoServiceImpl implements EventDaoService {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Event> findEvents(SearchEventsRequest request) {
        List<Event> events = new ArrayList<>();
        StringJoiner sqlJoiner = new StringJoiner(" ");
        Map<String, Object> paramMap = new HashMap<>();
        sqlJoiner.add("SELECT * FROM events");

        StringJoiner conditionsJoiner = new StringJoiner(" AND ");
        try {
            if (request.getStartDateTime() != null) {
                paramMap.put("startDateTime",getTimeStamp(getDateIfDateOrTimeIsNull(request.getStartDateTime())));
                conditionsJoiner.add("(date_time >= :startDateTime)");
            }
            if (request.getEndDateTime() != null) {
                paramMap.put("endDateTime", getTimeStamp(getDateIfDateOrTimeIsNull(request.getEndDateTime())));
                conditionsJoiner.add("(date_time <= :endDateTime)");
            } else {
                LocalDate futureDate = LocalDate.now().plusMonths(1);
                paramMap.put("endDateTime", futureDate);
                conditionsJoiner.add("(date_time <= :endDateTime)");
            }
        } catch (NullPointerException | ParseException e) {
            e.printStackTrace();
        }
        if (request.getTypeId() != null) {
            conditionsJoiner.add("type_id=:typeId");
            paramMap.put("typeId", request.getTypeId());
        }
//        if (request.getRadius() != null && request.getCentreLatitude() != null && request.getCentreLongitude() != null) {
//            conditionsJoiner.add("(distance(:cLatitude,:cLongitude,latitude,longitude)<= :radius)");
//            paramMap.put("cLatitude", request.getCentreLatitude());
//            paramMap.put("cLongitude", request.getCentreLongitude());
//            paramMap.put("radius", request.getRadius());
//        }
        if (request.getRadius() != null) {
            if (request.getCentreLatitude() != null) {
                if (request.getCentreLongitude() != null) {
                    conditionsJoiner.add("(distance(:cLatitude,:cLongitude,latitude,longitude)<= :radius)");
                    paramMap.put("cLatitude", request.getCentreLatitude());
                    paramMap.put("cLongitude", request.getCentreLongitude());
                    paramMap.put("radius", request.getRadius());
                }
            }
        }
        conditionsJoiner.add("(NOT deleted)");
        if (request.isPaid()) {
            conditionsJoiner.add("(cost IS NOT NULL AND cost > 0)");
        } else {
            conditionsJoiner.add("(cost IS NULL OR cost = 0)");
        }
        conditionsJoiner.add("(silence = :silence)");
        paramMap.put("silence", request.isSilence());
        if (conditionsJoiner.length() > 0) {
            sqlJoiner.add(" WHERE ");
        }
        sqlJoiner.add(conditionsJoiner.toString());

        namedParameterJdbcTemplate.query(sqlJoiner.toString(), paramMap, rs -> {
            if (!rs.wasNull()) {
                return;
            }
            do {
                events.add(Event.builder()
                        .id(rs.getInt("id"))
                        .latitude(rs.getBigDecimal("latitude"))
                        .longitude(rs.getBigDecimal("longitude"))
                        .address(rs.getString("address"))
                        .typeId(rs.getInt("type_id"))
                        .ownerAge(rs.getInt("owner_age"))
                        .dateTime(getDate(rs.getTimestamp("date_time")))
                        .fromParticipantCount(rs.getObject("from_count", Integer.class))
                        .toParticipantCount(rs.getObject("to_count", Integer.class))
                        .comment(rs.getString("comment"))
                        .targetAgeFrom(rs.getObject("target_age_from", Integer.class))
                        .targetAgeTo(rs.getObject("target_age_to", Integer.class))
                        .created(getDate(rs.getTimestamp("created")))
                        .cost(rs.getObject("cost", Integer.class))
                        .phone(rs.getString("phone"))
                        .silence(rs.getBoolean("silence"))
                        .build());
            } while (rs.next());
        });

        return events;
    }

    @Override
    @Transactional
    public int createEvent(CreateEventRequest request) {
        AtomicInteger eventId = new AtomicInteger();

        jdbcTemplate.query("INSERT INTO events (latitude, longitude, address, owner_age, date_time, from_count, to_count, comment, created, target_age_from, target_age_to, type_id, cost, silence, phone) " +
                        "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING id", ps -> {
                    ps.setObject(1, request.getLatitude(), Types.DECIMAL);
                    ps.setObject(2, request.getLongitude(), Types.DECIMAL);
                    ps.setString(3, request.getAddress());
                    ps.setInt(4, request.getOwnerAge());
                    ps.setTimestamp(5, getTimeStamp(request.getDateTime()));
                    ps.setObject(6, request.getMinParticipantCount(), Types.INTEGER);
                    ps.setObject(7, request.getMaxParticipantCount(), Types.INTEGER);
                    ps.setString(8, request.getComment());
                    ps.setTimestamp(9, new Timestamp(Instant.now().toEpochMilli()));
                    ps.setObject(10, request.getMinAgeOfParticipants(), Types.INTEGER);
                    ps.setObject(11, request.getMaxAgeOfParticipants(), Types.INTEGER);
                    ps.setInt(12, request.getTypeId());
                    ps.setInt(13, request.getCost());
                    ps.setBoolean(14, request.isSilence());
                    ps.setString(15, request.getPhone());
                }, rs -> {
                    eventId.set(rs.getInt("id"));
                }
        );
        return eventId.get();
    }

    @Override
    public void deactivateAllEvents() {
        jdbcTemplate.update("UPDATE events SET deleted = true");
        jdbcTemplate.update("UPDATE event_types SET count = 0");
    }

    private static Timestamp getTimeStamp(Date dateTime) {
        return new Timestamp(dateTime.toInstant().toEpochMilli());
    }

    private static Date getDate(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }

    private static Date getDateIfDateOrTimeIsNull(Date date) throws ParseException {
        String onlyDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String onlyTime = new SimpleDateFormat("HH:mm:ss").format(date);
        if (onlyDate.equals("2021-07-10") || onlyDate.equals("2021-07-20")) {
            onlyDate = String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(date).toString());
        } else if (onlyTime.equals("00:00:00")) {
            onlyTime = String.valueOf(new Time(date.getTime()));
            String newDate = onlyDate + " " + onlyTime + new SimpleDateFormat("XXX").format(date);
            return new SimpleDateFormat().parse(newDate);
        }
        return date;
    }
// timezone-i het kapvac xndire mnacel 
}