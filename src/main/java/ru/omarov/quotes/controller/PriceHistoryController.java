package ru.omarov.quotes.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.omarov.quotes.entity.PriceHistory;
import ru.omarov.quotes.services.PriceHistoryService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/history")
public class PriceHistoryController {
    private final PriceHistoryService priceHistoryService;

    public PriceHistoryController(PriceHistoryService priceHistoryService) {
        this.priceHistoryService = priceHistoryService;
    }

    @CrossOrigin
    @GetMapping("/")
    public Page<PriceHistory> getHistory(@RequestParam("p") Integer page,
                                         @RequestParam("els") Integer elements) {
        return priceHistoryService.getAll(page, elements);
    }

    @CrossOrigin
    @GetMapping("/diff")
    public BigDecimal getDiffBetween(LocalDate start, LocalDate stop, String uid) {
        return priceHistoryService.getDiffBetween(start, stop, uid);
    }

    @CrossOrigin
    @GetMapping("/price_history_between")
    public List<PriceHistory> getPriceHistoryByUidBetween(String uid,
                                                          LocalDate start,
                                                          LocalDate stop) {
        return priceHistoryService.getAllBetweenByUid(uid, start, stop);
    }
}
