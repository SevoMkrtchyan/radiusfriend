package org.radiusfriend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

import static org.radiusfriend.format.Patterns.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
@Schema(description = "Сущность события")
public class Event {
    @Schema(description = "Идентификатор события")
    private int id;

    @Schema(description = "Имя события")
    private String name;

    @Schema(description = "Широта места события")
    private BigDecimal latitude;

    @Schema(description = "Долгота места события")
    private BigDecimal longitude;

    @Schema(description = "Адрес события")
    private String address;

    @Schema(description = "Идентификатор типа события")
    private int typeId;

    @Schema(description = "Возраст создателя события")
    private int ownerAge;

    @Schema(description = "Дата/время проведения (по Гринвичу)", example = DATE_EXAMPLE2)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private Date dateTime;

    @Schema(description = "Минимальное количество участников")
    private Integer fromParticipantCount;

    @Schema(description = "Максимальное количество участников")
    private Integer toParticipantCount;

    @Schema(description = "Минимальный возраст участников")
    private Integer targetAgeFrom;

    @Schema(description = "Максимальный возраст участников")
    private Integer targetAgeTo;

    @Schema(description = "Комментарий о событии")
    private String comment;

    @Schema(description = "Дата/время создания события (по Гринвичу)")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date created;

    @Schema(description = "Цена билета, если событие платное")
    private Integer cost;

    @Schema(description = "Не против неловкого молчания")
    private boolean silence;

    @Schema(description = "Номер телефона")
    private String phone;
}