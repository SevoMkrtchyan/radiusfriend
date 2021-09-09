package org.radiusfriend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.radiusfriend.dto.EventTypeDto;
import org.radiusfriend.service.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

import static org.radiusfriend.consts.WebConsts.EVENT_TYPES;

@RestController
@RequestMapping(value = EVENT_TYPES)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Tag(name = "Контроллер типов событий", description = "Управление типами событий")
public class EventTypeController {
    private final EventTypeService eventTypeService;

    @GetMapping(value = "typesLikeName", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Поиск типов событий",
            description = "Позволяет регистронезависимо искать подходящие типы событий по заданной части имени")
    public ResponseEntity<List<EventTypeDto>> getTypesLikeName(@RequestParam(value = "namePart", required = false, defaultValue = "") @Min(1)
                                                               @Parameter(description = "Часть имени события, введенная в любом регистре")
                                                                       String namePart) {
        log.debug("Event types searching for part={}", namePart);
        val typesByRate = eventTypeService.getTypesByRate(namePart);
        log.debug("Found types={}", typesByRate);
        return ResponseEntity.ok(typesByRate);
    }
}