package ru.omarov.quotes.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.omarov.quotes.entity.Position;
import ru.omarov.quotes.entity.PositionType;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PositionRepo extends JpaRepository<Position, String> {
    Page<Position> findByType(PositionType type, PageRequest limit);

    Position findByUid(String uid);

    Position findByName(String name);

    List<Position> findByCurrency(String currency);

    List<Position> findByExchange(String exchange);

    List<Position> findByNominal(BigDecimal price);

    List<Position> findBySector(String sector);

    List<Position> findByTicker(String ticker);
}
