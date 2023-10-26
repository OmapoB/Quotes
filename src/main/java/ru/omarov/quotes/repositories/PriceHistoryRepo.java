package ru.omarov.quotes.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.omarov.quotes.entity.Position;
import ru.omarov.quotes.entity.PriceHistory;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PriceHistoryRepo extends JpaRepository<PriceHistory, Date> {
    void deleteAllByPosition(Position position);

    PriceHistory findByDayAndPositionUid(LocalDate date, String uid);

    List<PriceHistory> findAllByPositionUidAndDayBetween(String uid, LocalDate start, LocalDate end);
}
