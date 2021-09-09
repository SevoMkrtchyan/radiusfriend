package org.radiusfriend.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Date;

import static org.radiusfriend.format.Patterns.DATE_EXAMPLE1;
import static org.radiusfriend.format.Patterns.DATE_EXAMPLE2;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class SearchEventsRequest {
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    @Schema(description = "Широта центра области поиска")
    private BigDecimal centreLatitude;

    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    @Schema(description = "Долгота центра области поиска")
    private BigDecimal centreLongitude;

    @Positive
    @Schema(description = "Радиус области поиска (в метрах)")
    private BigDecimal radius;

    @Min(value = 0, message = "Возраст должен быть больше 0")
    @Schema(description = "Возраст пользователя (в годах)")
    private Integer userAge;

    @Min(value = 1, message = "Идентификатор должен быть больше 1")
    @Schema(description = "Идентификатор типа события")
    private Integer typeId;

    @Schema(description = "Дата/время начала (по умолчанию - сейчас)",example = DATE_EXAMPLE1)
    private Date startDateTime;

    @Schema(description = "Дата/время завершения",example = DATE_EXAMPLE2)
    private Date endDateTime;

    @Schema(description = "Минимальное количество участников (по умолчанию - 1)")
    @Min(1)
    private int minParticipantCount = 1;

    @Schema(description = "Максимальное количество участников")
    @Min(1)
    private Integer maxParticipantCount;

    @Schema(description = "Не против неловкого молчания. По дефолту - да, не против")
    private boolean silence = true;

    @Schema(description = "Признак платности событий")
    private boolean paid = false;
}