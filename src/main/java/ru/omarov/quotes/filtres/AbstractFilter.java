package ru.omarov.quotes.filtres;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public abstract class AbstractFilter<T> implements Filterable<T> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<T> getFilteredElementsPage(Map<String, String> filterBy, Pageable pageRequest) {
        List<T> filteredElements = getFilteredElements(filterBy, pageRequest);
        Long totalCount = getTotalCount(filterBy);
        return new PageImpl<>(filteredElements, pageRequest, totalCount);
    }

    @Override
    public List<T> getFilteredElements(Map<String, String> filterBy, Pageable limits) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(getEntityType());
        Root<T> root = query.from(getEntityType());
        Predicate predicate = buildPredicate(filterBy, criteriaBuilder, root);
        query.where(predicate);
        return entityManager.createQuery(query)
                .setFirstResult((int) limits.getOffset())
                .setMaxResults(limits.getPageSize())
                .getResultList();
    }

    private Long getTotalCount(Map<String, String> filterBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<T> root = query.from(getEntityType());
        Predicate predicate = buildPredicate(filterBy, criteriaBuilder, root);
        query.select(criteriaBuilder.count(root)).where(predicate);
        return entityManager.createQuery(query).getSingleResult();
    }

    protected abstract Predicate buildPredicate(Map<String, String> filterBy, CriteriaBuilder cb, Root<T> root);

    protected abstract Class<T> getEntityType();
}
