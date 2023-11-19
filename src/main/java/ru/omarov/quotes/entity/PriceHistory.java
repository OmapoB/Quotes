package ru.omarov.quotes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class PriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate day;
    private BigDecimal nominal;
    private String positionUid;// сделать uid убрать связь

    public PriceHistory(LocalDate day, BigDecimal nominal, String position) {
        this.day = day;
        this.nominal = nominal;
        this.positionUid = position;
    }
}
