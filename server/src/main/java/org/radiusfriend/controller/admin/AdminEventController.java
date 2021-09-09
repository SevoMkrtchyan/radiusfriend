package org.radiusfriend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.radiusfriend.controller.response.BaseResponse;
import org.radiusfriend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.radiusfriend.consts.WebConsts.ADMIN;

@RestController
@RequestMapping(value = ADMIN)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Tag(name = "Контроллер типов событий", description = "Управление типами событий")
public class AdminEventController {
    private final EventService eventService;

    @DeleteMapping("allEvents")
    @Operation(summary = "Деактивация событий",
            description = "Позволяет деактивировать все существующие события")
    public ResponseEntity<BaseResponse> deactivateAllEvents() {
        eventService.deactivateAllEvents();
        return ResponseEntity.ok(new BaseResponse());
    }
}