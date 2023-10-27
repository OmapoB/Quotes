package ru.omarov.quotes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    // Тип позиции
    private PositionType type;
    // Тикер инструмента.
    private String ticker;
    // Класс-код (секция торгов).
    private String classCode;
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
