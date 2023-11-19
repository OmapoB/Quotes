package ru.omarov.quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.omarov.quotes.entity.PriceHistory;

import java.sql.Date;
import java.time.LocalDate;

@Repository
public interface PriceHistoryRepo extends JpaRepository<PriceHistory, Date> {
    PriceHistory findByPositionUidAndDay(String uid, LocalDate date);
}
