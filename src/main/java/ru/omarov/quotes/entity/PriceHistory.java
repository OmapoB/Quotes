package ru.omarov.quotes.entity;

import jakarta.persistence.*;
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
    @ManyToOne
    private Position position;

    public PriceHistory(LocalDate day, BigDecimal nominal, Position position) {
        this.day = day;
        this.nominal = nominal;
        this.position = position;
    }
}
