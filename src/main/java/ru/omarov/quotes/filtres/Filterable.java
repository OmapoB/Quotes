package ru.omarov.quotes.filtres;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface Filterable<T> {
    Page<T> getFilteredElementsPage(Map<String, String> filterBy, Pageable pageRequest);

    List<T> getFilteredElements(Map<String, String> filterBy, Pageable limits);
}
