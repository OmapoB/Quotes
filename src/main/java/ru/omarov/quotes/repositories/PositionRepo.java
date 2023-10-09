package ru.omarov.quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.omarov.quotes.entity.Position;
import ru.omarov.quotes.entity.PositionType;

import java.util.List;

public interface PositionRepo extends JpaRepository<Position, String> {
    public List<Position> getByType(PositionType type);
    public Position getByUid(String uid);
    public Position getByName(String name);
    public List<Position> getByCurrency(String currency);
    public Position getByIsin(String isin);
    public Position getByExchange(String exchange);
    public Position getBySector(String sector);
}
