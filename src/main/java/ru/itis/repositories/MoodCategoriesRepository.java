package ru.itis.repositories;

import ru.itis.models.MoodCategory;

import java.util.List;

public interface MoodCategoriesRepository {
    void save(MoodCategory moodCategory);
    List<MoodCategory> findAll();
    void update(MoodCategory moodCategory);
    void delete(Long id);
    List<MoodCategory> findByName(String name);
}
