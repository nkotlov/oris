package ru.itis.repositories;

import ru.itis.models.MoodCategory;

import java.util.List;

public interface MoodCategoryRepository extends CrudRepository<MoodCategory> {
    List<Long> findPlaylistsByCategoryId(Long categoryId);
    void addPlaylistToCategory(Long categoryId, Long playlistId);
    void removePlaylistFromCategory(Long categoryId, Long playlistId);
}
