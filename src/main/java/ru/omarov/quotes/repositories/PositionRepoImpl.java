package ru.omarov.quotes.repositories;

import com.google.protobuf.GeneratedMessageV3;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.omarov.quotes.entity.Position;
import ru.omarov.quotes.entity.PositionType;
import ru.tinkoff.piapi.contract.v1.MoneyValue;
import ru.tinkoff.piapi.contract.v1.Quotation;

import java.util.Arrays;
import java.util.List;

public class PositionRepoImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public <T extends GeneratedMessageV3> Position create(T position) {
        Position newPosition = new Position();

        newPosition.setType(Arrays.stream(PositionType.values())
                .filter(s->s.getaClass()==position.getClass())
                .findFirst().orElse(null));
        newPosition
                .setFigi(position.getField(position.getDescriptorForType()
                        .findFieldByName("figi")).toString());
        newPosition
                .setTicker(position.getField(position.getDescriptorForType()
                        .findFieldByName("ticker")).toString());
        newPosition
                .setClassCode(position.getField(position.getDescriptorForType()
                        .findFieldByName("class_code")).toString());
        newPosition
                .setIsin(position.getField(position.getDescriptorForType()
                        .findFieldByName("isin")).toString());
        newPosition
                .setLot(Integer.parseInt(position.getField(position.getDescriptorForType()
                        .findFieldByName("lot")).toString()));
        newPosition
                .setCurrency(position.getField(position.getDescriptorForType()
                        .findFieldByName("currency")).toString());
        newPosition
                .setName(position.getField(position.getDescriptorForType()
                        .findFieldByName("name")).toString());
        newPosition
                .setExchange(position.getField(position.getDescriptorForType()
                        .findFieldByName("exchange")).toString());
        newPosition
                .setSector(position.getField(position.getDescriptorForType()
                        .findFieldByName("sector")).toString());
        newPosition
                .setNominal((MoneyValue) position.getField(position.getDescriptorForType()
                        .findFieldByName("nominal")));
        newPosition
                .setMinPriceIncrement((Quotation) position.getField(position.getDescriptorForType()
                        .findFieldByName("min_price_increment")));
        newPosition
                .setUid(position.getField(position.getDescriptorForType()
                        .findFieldByName("uid")).toString());
        entityManager.persist(newPosition);
        return newPosition;
    }


    public Position get(Position position) {
        return null;
    }


    public List<Position> getByType(PositionType type) {
        return null;
    }


    public Position getByUid(String uid) {
        return null;
    }

    public Position getByName(String name) {
        return null;
    }


    public List<Position> getByCurrency(String currency) {
        return null;
    }

    public Position getByIsin(String isin) {
        return null;
    }

    public Position getByExchange(String exchange) {
        return null;
    }

    public Position getBySector(String sector) {
        return null;
    }

    public Position update(Position position) {
        return null;
    }

    public Boolean delete(Position position) {
        return null;
    }
}
