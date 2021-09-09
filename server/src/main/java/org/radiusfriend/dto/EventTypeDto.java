package org.radiusfriend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@Schema(description = "Сущность типа события")
public class EventTypeDto {
    @Schema(description = "Тип события")
    private final String name;

    @Schema(description = "Идентификатор типа события")
    private final Integer id;
}