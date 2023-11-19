package ru.omarov.quotes.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.omarov.quotes.entity.PriceHistory;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.contract.v1.InstrumentStatus;
import ru.tinkoff.piapi.contract.v1.Quotation;
import ru.tinkoff.piapi.core.InvestApi;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class InitService {

    @Value("${token}")
    private String token;

    private final PositionService positionService;
    private final PriceHistoryService priceHistoryService;

    private InvestApi api;

    public InitService(PositionService positionService, PriceHistoryService priceHistoryService) {
        this.positionService = positionService;
        this.priceHistoryService = priceHistoryService;
    }

    @PostConstruct
    public void postConstruct() {
        api = InvestApi.createSandbox(token);
    }

    public void initializePositions() throws ExecutionException, InterruptedException {
        api.getInstrumentsService()
                .getShares(InstrumentStatus.INSTRUMENT_STATUS_BASE)
                .get()
                .forEach(position -> {
                    try {
                        positionService.updateOrCreate(position);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public void initializePriceHistoryByPositionUid(String uid) throws ExecutionException, InterruptedException {
        Instant now = Instant.now();
        Instant period = now.minus(365, ChronoUnit.DAYS);
        List<HistoricCandle> historicCandles = api.getMarketDataService()
                .getCandles(
                        uid,
                        period,
                        now,
                        CandleInterval.CANDLE_INTERVAL_DAY
                )
                .get();
        for (HistoricCandle price : historicCandles) {
            Quotation newPrice = price.getClose();
            PriceHistory newNote = new PriceHistory(
                    LocalDate.ofInstant(Instant.ofEpochSecond(price.getTime().getSeconds()), ZoneId.systemDefault()),
                    newPrice.getUnits() == 0 && newPrice.getNano() == 0 ?
                            BigDecimal.ZERO :
                            BigDecimal.valueOf(newPrice.getUnits())
                                    .add(BigDecimal.valueOf(newPrice.getNano(), 9)),
                    uid
            );
            priceHistoryService.save(newNote);
        }
    }
}
