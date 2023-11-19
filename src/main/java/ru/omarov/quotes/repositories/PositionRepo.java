package ru.omarov.quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.omarov.quotes.entity.Position;

@Repository
public interface PositionRepo extends JpaRepository<Position, String> {
    Position findByUid(String uid);

    Position findByName(String name);
}
