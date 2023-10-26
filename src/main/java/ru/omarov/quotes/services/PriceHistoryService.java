package ru.omarov.quotes.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.omarov.quotes.entity.PriceHistory;
import ru.omarov.quotes.repositories.PriceHistoryRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class PriceHistoryService {

    private PriceHistoryRepo priceHistoryRepo;

    public PriceHistoryService(PriceHistoryRepo priceHistoryRepo) {
        this.priceHistoryRepo = priceHistoryRepo;
    }

//    @Transactional
//    public void initializePriceHistory(String uid, Map<LocalDate, BigDecimal> priceHistory) {
//        Position position = positionService.getByUid(uid);
//        for (Map.Entry<LocalDate, BigDecimal> priceAt :
//                priceHistory.entrySet()) {
//            PriceHistory price = new PriceHistory();
//            price.setDay(priceAt.getKey());
//            price.setNominal(priceAt.getValue());
//            price.setPosition(position);
//        }
//    }

    @Transactional
    public PriceHistory save(PriceHistory priceHistory) {
        return priceHistoryRepo.saveAndFlush(priceHistory);
    }

    public Page<PriceHistory> getAll(Integer page, Integer elements) {
        return priceHistoryRepo.findAll(PageRequest.of(page, elements));
    }

    public List<PriceHistory> getAllBetweenByUid(String uid, LocalDate start, LocalDate end) {
        return priceHistoryRepo.findAllByPositionUidAndDayBetween(uid, start, end);
    }

    public BigDecimal getDiffBetween(LocalDate start, LocalDate stop, String uid) {
        BigDecimal nominal = priceHistoryRepo
                .findByDayAndPositionUid(start, uid)
                .getNominal();
        BigDecimal nominal1 = priceHistoryRepo
                .findByDayAndPositionUid(stop, uid)
                .getNominal();
        return nominal1.divide(nominal, RoundingMode.HALF_EVEN).subtract(BigDecimal.valueOf(1L));
    }
}
