package ru.omarov.quotes.services;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface Service<T> {
    void save(T toSave);

    List<T> getAll();

    Page<T> getFilteredElements(Map<String, String> filterBy);
}
