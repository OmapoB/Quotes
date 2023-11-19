package ru.omarov.quotes.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.omarov.quotes.entity.PriceHistory;
import ru.omarov.quotes.services.PriceHistoryService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/history")
public class PriceHistoryController {
    private final PriceHistoryService priceHistoryService;

    public PriceHistoryController(PriceHistoryService priceHistoryService) {
        this.priceHistoryService = priceHistoryService;
    }

    @Operation(summary = "получение разницы в цене в процентах")
    @CrossOrigin
    @GetMapping("/percentage_difference")
    public BigDecimal getPercentageDifferenceBetween(
            @RequestParam String uid,
            @RequestParam LocalDate start,
            @RequestParam LocalDate stop) {
        return priceHistoryService.getPercentageDiffBetween(uid, start, stop);
    }

    @Operation(summary = "постраничное получение отфильтрованной истории цен по дням")
    @CrossOrigin
    @GetMapping("")
    public Page<PriceHistory> getFilteredHistory(
            @RequestParam Map<String, String> filterBy) {
        return priceHistoryService.getFilteredElements(filterBy);
    }
}
