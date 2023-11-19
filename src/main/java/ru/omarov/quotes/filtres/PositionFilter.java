package ru.omarov.quotes.filtres;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;
import ru.omarov.quotes.entity.Position;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PositionFilter extends AbstractFilter<Position> {
    @Override
    protected Predicate buildPredicate(Map<String, String> filterBy, CriteriaBuilder cb, Root<Position> root) {
        List<Predicate> predicates = new ArrayList<>();
        filterBy.forEach((key, value) -> {
            if (key.equals("nominal")) {
                String start = "0";
                String stop = value;
                String[] split = value.split(",");
                if (split.length == 2) {
                    start = split[0];
                    stop = split[1];
                }
                predicates.add(cb.between(root.get(key), new BigDecimal(start), new BigDecimal(stop)));
            } else
                predicates.add(cb.equal(root.get(key), value));
        });
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    @Override
    protected Class<Position> getEntityType() {
        return Position.class;
    }
}
