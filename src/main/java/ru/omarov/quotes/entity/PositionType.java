package ru.omarov.quotes.entity;

import ru.tinkoff.piapi.contract.v1.*;

public enum PositionType {
    BOND("Облигация", Bond.class),
    CURRENCY("Валюта", Currency.class),
    ETF("Фонд", Etf.class),
    FUTURE("Фьючерс", Future.class),
    OPTION("Опцион", Option.class),
    SHARE("Акция", Share.class);
    private final String name;

    private final Class aClass;

    PositionType(String name, Class aClass) {
        this.name = name;
        this.aClass = aClass;
    }

    public String getName() {
        return name;
    }

    public Class getaClass() {
        return aClass;
    }
}
