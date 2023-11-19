package ru.omarov.quotes.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.omarov.quotes.filtres.Filterable;

import java.util.List;
import java.util.Map;

public abstract class AbstractService<T> implements Service<T> {
    protected static final int DEFAULT_POSITIONS_ON_PAGE = 20;
    protected Filterable<T> filter;

    @Override
    public void save(T toSave) {
    }

    @Override
    public List<T> getAll() {
        return null;
    }

    @Override
    public Page<T> getFilteredElements(Map<String, String> filterBy) {
        String toCheck = filterBy.getOrDefault("pageSize", DEFAULT_POSITIONS_ON_PAGE + "");
        int pageSize = toCheck.equals("all") ? Integer.MAX_VALUE : Integer.parseInt(toCheck);
        PageRequest pageRequest = PageRequest.of(
                Integer.parseInt(filterBy.getOrDefault("pageNumber", "0")),
                pageSize
        );
        filterBy.remove("pageNumber");
        filterBy.remove("pageSize");
        return filter.getFilteredElementsPage(filterBy, pageRequest);
    }
}
