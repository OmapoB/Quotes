package ru.omarov.quotes.repositories;

import ru.omarov.quotes.entity.Position;
import ru.omarov.quotes.entity.PositionType;

import java.util.List;

public interface PositionRepo {
    <T> Position create(T position);

    Position get(Position position);

    List<Position> getByType(PositionType type);

    Position getByUid(String uid);

    Position getByName(String name);

    List<Position> getByCurrency(String currency);

    Position getByIsin(String isin);

    Position getByExchange(String exchange);

    Position getBySector(String sector);

    Position update(Position position);

    Boolean delete(Position position);
}
