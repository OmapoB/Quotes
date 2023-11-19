package ru.omarov.quotes.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.omarov.quotes.entity.Position;
import ru.omarov.quotes.services.PositionService;

import java.util.Map;

@RestController
@RequestMapping("/positions")
public class PositionController {
    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @Operation(summary = "получение позиции по уникальному идентификатору")
    @CrossOrigin
    @GetMapping("/{id}")
    public Position getByUid(@PathVariable(name = "id") String uid) {
        return positionService.getByUid(uid);
    }

    @Operation(summary = "постраничное получение отфильтрованных позиций")
    @CrossOrigin
    @GetMapping("")
    public Page<Position> getFilteredPositions(
            @RequestParam Map<String, String> params) {
        return positionService.getFilteredElements(params);
    }
}
