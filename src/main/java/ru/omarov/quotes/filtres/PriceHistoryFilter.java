package ru.omarov.quotes.filtres;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;
import ru.omarov.quotes.entity.PriceHistory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PriceHistoryFilter extends AbstractFilter<PriceHistory> {
    record DateInfo(int day, int month, int year) {
        public DateInfo(String[] dateString) {
            this(
                    Integer.parseInt(dateString[0]),
                    Integer.parseInt(dateString[1]),
                    Integer.parseInt(dateString[2])
            );
        }

        public LocalDate convert() {
            return LocalDate.of(year, month, day);
        }
    }

    @Override
    protected Predicate buildPredicate(Map<String, String> filterBy, CriteriaBuilder cb, Root<PriceHistory> root) {
        List<Predicate> predicates = new ArrayList<>();
        filterBy.forEach((key, value) -> {
            String[] split = value.split(",");
            if (split.length > 1) {
                DateInfo startDate = new DateInfo(split[0].split("\\."));
                DateInfo stopDate = new DateInfo(split[1].split("\\."));
                predicates.add(cb.between(root.get(key), startDate.convert(), stopDate.convert()));
            } else predicates.add(cb.equal(root.get(key), value));
        });
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    @Override
    protected Class<PriceHistory> getEntityType() {
        return PriceHistory.class;
    }
}