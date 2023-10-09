package ru.omarov.quotes.repositories;

import com.google.protobuf.GeneratedMessageV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.omarov.quotes.entity.Position;
import ru.omarov.quotes.entity.PositionType;
import ru.tinkoff.piapi.contract.v1.MoneyValue;
import ru.tinkoff.piapi.contract.v1.Quotation;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Repository
public class PositionDTO {

    @Autowired
    private PositionRepo repos;

    public PositionDTO() {
    }

    public <T extends GeneratedMessageV3> Position create(T position) {
        Position newPosition = new Position();

        newPosition.setType(Arrays.stream(PositionType.values())
                .filter(s -> s.getaClass() == position.getClass())
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

        MoneyValue moneyValue = (MoneyValue) position.getField(position.getDescriptorForType()
                .findFieldByName("nominal"));
        newPosition
                .setNominal(moneyValue.getUnits() == 0 && moneyValue.getNano() == 0 ?
                        BigDecimal.ZERO : BigDecimal.valueOf(moneyValue.getUnits())
                        .add(BigDecimal.valueOf(moneyValue.getNano(), 9)));

        Quotation quotation =(Quotation) position.getField(position.getDescriptorForType()
                .findFieldByName("min_price_increment"));
        newPosition
                .setMinPriceIncrement(quotation.getUnits() == 0 && quotation.getNano() == 0 ?
                        BigDecimal.ZERO : BigDecimal.valueOf(quotation.getUnits())
                        .add(BigDecimal.valueOf(quotation.getNano(), 9)));
        newPosition
                .setUid(position.getField(position.getDescriptorForType()
                        .findFieldByName("uid")).toString());
        repos.save(newPosition);
        return newPosition;
    }

    public <T extends GeneratedMessageV3> Position update(T position) {
        return create(position);
    }

    public List<Position> getAll() {
        return repos.findAll();
    }

    public List<Position> getByType(PositionType type) {
        return repos.getByType(type);
    }

    public Position getByUid(String uid) {
        return repos.getByUid(uid);
    }

    public Position getByName(String name) {
        return repos.getByName(name);
    }

    public List<Position> getByCurrency(String currency) {
        return repos.getByCurrency(currency);
    }

    public Position getByIsin(String isin) {
        return repos.getByIsin(isin);
    }

    public Position getByExchange(String exchange) {
        return repos.getByExchange(exchange);
    }

    public Position getBySector(String sector) {
        return repos.getBySector(sector);
    }
}
