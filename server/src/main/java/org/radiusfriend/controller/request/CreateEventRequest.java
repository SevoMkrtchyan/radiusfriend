package org.radiusfriend.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

import static org.radiusfriend.format.Patterns.DATE_EXAMPLE2;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class CreateEventRequest {
    @NotEmpty(message = "Введите имя события")
    @NotNull(message = "Введите имя события")
    @Schema(description = "Имя события", example = "Рок-концерт")
    private String name;

    @NotNull(message = "Введите координаты события")
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    @Schema(description = "Широта")
    private BigDecimal latitude;

    @NotNull(message = "Введите координаты события")
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    @Schema(description = "Долгота")
    private BigDecimal longitude;

    @Min(0)
    @Schema(description = "Цена, если событие платное")
    private int cost = 0;

    @Schema(description = "Адрес")
    private String address;

    @Min(value = 1, message = "Невозможно привязать к типу события")
    @Schema(description = "Идентификатор типа события, если такие уже создавались")
    private Integer typeId;

    @Min(value = 0, message = "Возраст участников должен быть больше 0 лет")
    @NotNull(message = "Возраст участников должен быть больше 0 лет")
    @Schema(description = "Возраст создателя события (в годах)")
    private Integer ownerAge;

    @NotNull(message = "Введите дату события")
    @Schema(description = "Дата/время начала", example = DATE_EXAMPLE2)
    private Date dateTime;

    @Min(value = 1, message = "Число участников должно быть больше 1")
    @Schema(description = "Минимальное количество участников")
    private Integer minParticipantCount;

    @Min(value = 1, message = "Число участников должно быть больше 1")
    @Schema(description = "Максимальное количество участников")
    private Integer maxParticipantCount;

    @Min(value = 0, message = "Возраст участников должен быть больше 0 лет")
    @Schema(description = "Минимальный возраст (в годах)", example = "18")
    private Integer minAgeOfParticipants;

    @Min(value = 0, message = "Возраст участников должен быть больше 0 лет")
    @Schema(description = "Максимальный возраст (в годах)", example = "200")
    private Integer maxAgeOfParticipants;

    @Schema(description = "Комментарий", example = "Соседи точно придут")
    private String comment;

    @Schema(description = "Номер телефона")
    @Size(max = 36)
    private String phone;

    @Schema(description = "Не против неловкого молчания. По дефолту - да, не против")
    private boolean silence = true;
}