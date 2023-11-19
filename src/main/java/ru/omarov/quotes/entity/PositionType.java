package ru.omarov.quotes.entity;

import com.google.protobuf.GeneratedMessageV3;
import ru.tinkoff.piapi.contract.v1.*;

import java.util.Arrays;
import java.util.Optional;

public enum PositionType {
    BOND("Облигация", Bond.class),
    CURRENCY("Валюта", Currency.class),
    ETF("Фонд", Etf.class),
    FUTURE("Фьючерс", Future.class),
    OPTION("Опцион", Option.class),
    SHARE("Акция", Share.class);
    private final String name;

    private final Class<? extends GeneratedMessageV3> type;

    PositionType(String name, Class<? extends GeneratedMessageV3> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<? extends GeneratedMessageV3> getType() {
        return type;
    }

    public static Optional<PositionType> getByType(Class<? extends GeneratedMessageV3> cl) {
        return Arrays.stream(PositionType.values()).filter(s -> s.getType() == cl).findFirst();

    }
}
