package ru.omarov.quotes.services;

import org.springframework.stereotype.Service;
import ru.omarov.quotes.entity.PriceHistory;
import ru.omarov.quotes.filtres.PriceHistoryFilter;
import ru.omarov.quotes.repositories.PriceHistoryRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class PriceHistoryService extends AbstractService<PriceHistory> {

    private final PriceHistoryRepo priceHistoryRepo;

    public PriceHistoryService(PriceHistoryRepo priceHistoryRepo, PriceHistoryFilter priceHistoryFilter) {
        this.priceHistoryRepo = priceHistoryRepo;
        super.filter = priceHistoryFilter;
    }

    public void save(PriceHistory priceHistory) {
        priceHistoryRepo.save(priceHistory);
    }

    public BigDecimal getPercentageDiffBetween(String uid, LocalDate start, LocalDate stop) {
        BigDecimal startPrice = priceHistoryRepo
                .findByPositionUidAndDay(uid, start)
                .getNominal();
        BigDecimal stopPrice = priceHistoryRepo
                .findByPositionUidAndDay(uid, stop)
                .getNominal();
        return stopPrice
                .divide(startPrice, RoundingMode.HALF_EVEN)
                .subtract(BigDecimal.valueOf(1L));
    }
}
