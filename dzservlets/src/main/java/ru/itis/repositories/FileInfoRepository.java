package ru.itis.repositories;

import ru.itis.models.FileInfo;

import java.util.List;

public interface FileInfoRepository extends CrudRepository<FileInfo> {
    List<FileInfo> findFilesByPurpose(String purpose);
    void attachFileToEntity(Long fileId, String entityName, Long entityId);
    void detachFileFromEntity(Long fileId, String entityName, Long entityId);
}
