package ru.itis.repositories;

import ru.itis.models.Collection;

import java.util.List;
import java.util.Optional;

public interface CollectionsRepository {
    void save(Collection collection);
    List<Collection> findAll();
    void update(Collection collection);
    void delete(Long id);
    List<Collection> findByName(String name);
    List<Collection> findByUserId(Long userId);
}
