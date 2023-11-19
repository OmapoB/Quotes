package ru.omarov.quotes.services;

import com.google.protobuf.GeneratedMessageV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.omarov.quotes.entity.Position;
import ru.omarov.quotes.entity.PositionType;
import ru.omarov.quotes.filtres.PositionFilter;
import ru.omarov.quotes.repositories.PositionRepo;
import ru.tinkoff.piapi.contract.v1.MoneyValue;
import ru.tinkoff.piapi.contract.v1.Quotation;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class PositionService extends AbstractService<Position> {
    public static final Integer DEFAULT_POSITIONS_ON_PAGE = 20;

    private final PositionRepo positionRepo;

    public PositionService(PositionRepo positionRepo, PositionFilter positionFilter) {
        this.positionRepo = positionRepo;
        super.filter = positionFilter;
    }


    @Transactional
    public void updateOrCreate(GeneratedMessageV3 position) throws IllegalAccessException {
        Position newPosition = new Position();

        newPosition.setType(PositionType.getByType(position.getClass()).orElseThrow());
        newPosition
                .setTicker(position.getField(position.getDescriptorForType()
                        .findFieldByName("ticker")).toString());
        newPosition
                .setClassCode(position.getField(position.getDescriptorForType()
                        .findFieldByName("class_code")).toString());
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

        Quotation quotation = (Quotation) position.getField(position.getDescriptorForType()
                .findFieldByName("min_price_increment"));
        newPosition
                .setMinPriceIncrement(quotation.getUnits() == 0 && quotation.getNano() == 0 ?
                        BigDecimal.ZERO : BigDecimal.valueOf(quotation.getUnits())
                        .add(BigDecimal.valueOf(quotation.getNano(), 9)));
        newPosition
                .setUid(position.getField(position.getDescriptorForType()
                        .findFieldByName("uid")).toString());
        positionRepo.save(newPosition);
    }

    public void save(Position position) {
        positionRepo.save(position);
    }

    public List<Position> getAll() {
        return positionRepo.findAll();
    }

    public Position getByUid(String uid) {
        return positionRepo.findByUid(uid);
    }
}
