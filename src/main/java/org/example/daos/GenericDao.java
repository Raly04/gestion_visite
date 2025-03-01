package org.example.daos;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GenericDao<T, ID> {
    void save(T entity);
    void update(T entity);
    void delete(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    List<T> find(Map<String, Object> criteria);
}
