package ru.omarov.quotes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.piapi.contract.v1.MoneyValue;
import ru.tinkoff.piapi.contract.v1.Quotation;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    // Тип позиции
    private PositionType type;
    // Figi-идентификатор инструмента.
    private String figi;
    // Тикер инструмента.
    private String ticker;
    // Класс-код (секция торгов).
    private String classCode;
    // Isin-идентификатор инструмента.
    private String isin;
    // Возможно совершение операций только на количества ценной бумаги, кратные параметру lot.
    private Integer lot;
    // Валюта расчётов.
    private String currency;
    // Название инструмента.
    private String name;
    // Реальная площадка исполнения расчётов (биржа).
    private String exchange;
    // Сектор экономики.
    private String sector;
    // Номинал.
    private BigDecimal nominal;
    // Шаг цены
    private BigDecimal minPriceIncrement;
    // Уникальный идентификатор инструмента.
    @Id
    private String uid;
}
