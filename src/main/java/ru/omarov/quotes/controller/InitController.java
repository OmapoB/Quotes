package ru.omarov.quotes.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.omarov.quotes.entity.PriceHistory;
import ru.omarov.quotes.services.PositionService;
import ru.omarov.quotes.services.PriceHistoryService;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.InstrumentStatus;
import ru.tinkoff.piapi.contract.v1.Quotation;
import ru.tinkoff.piapi.core.InvestApi;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/")
public class InitController {
    @Value("${token}")
    private String token;
    private PositionService positionService;
    private PriceHistoryService priceHistoryService;
    private InvestApi api = InvestApi.createSandbox("t.PtoWpdkmxbVnED4_plcINycHTThauIJTmjVK4CBrtRvdMVawzw0TKNtEw53QkXtxNjzPNcEwY_2DFuOlDB-5OA");

    public InitController(PositionService positionService, PriceHistoryService priceHistoryService) {
        this.positionService = positionService;
        this.priceHistoryService = priceHistoryService;
    }

    @GetMapping("/init0")
    public String init0() throws ExecutionException, InterruptedException {
        InvestApi api = InvestApi.createSandbox(token);
        api.getInstrumentsService()
                .getShares(InstrumentStatus.INSTRUMENT_STATUS_BASE)
                .get()
                .forEach(positionService::updateOrCreate);
        return "Positions has been initialized";
    }

    @GetMapping("/init1")
    public String init1() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            positionService.getAll(i, 202)
                    .getContent()
                    .forEach(position -> {
                        try {
                            api
                                    .getMarketDataService()
                                    .getCandles(position.getUid(),
                                            Instant.parse("2022-10-17T00:00:00.00Z"),
                                            Instant.parse("2023-10-17T00:00:00.00Z"),
                                            CandleInterval.CANDLE_INTERVAL_DAY)
                                    .get()
                                    .forEach(s -> {
                                        Quotation newPrice = s.getClose();
                                        PriceHistory newNote = new PriceHistory(Instant
                                                .ofEpochSecond(s
                                                        .getTime()
                                                        .getSeconds())
                                                .atZone(ZoneId.systemDefault())
                                                .toLocalDate(),
                                                newPrice.getUnits() == 0 && newPrice.getNano() == 0 ?
                                                        BigDecimal.ZERO : BigDecimal.valueOf(newPrice.getUnits())
                                                        .add(BigDecimal.valueOf(newPrice.getNano(), 9)),
                                                position);
                                        priceHistoryService.save(newNote);
                                    });
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        return "Price history has benn initialized";
    }
}
