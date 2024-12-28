package ru.itis.repositories.impl;

import ru.itis.models.FileInfo;
import ru.itis.repositories.FileInfoRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileInfoRepositoryJdbcImpl implements FileInfoRepository {
    private final DataSource dataSource;

    public FileInfoRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(FileInfo entity) {
        String sql = "INSERT INTO file_info (original_file_name, storage_file_name, size, type, purpose) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getOriginalFileName());
            statement.setString(2, entity.getStorageFileName());
            statement.setLong(3, entity.getSize());
            statement.setString(4, entity.getType());
            statement.setString(5, entity.getPurpose());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FileInfo> findById(Long id) {
        String sql = "SELECT * FROM file_info WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapRowToFileInfo(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<FileInfo> findAll() {
        String sql = "SELECT * FROM file_info";
        List<FileInfo> files = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                files.add(mapRowToFileInfo(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return files;
    }

    @Override
    public void update(FileInfo entity) {
        String sql = "UPDATE file_info SET original_file_name = ?, storage_file_name = ?, size = ?, type = ?, purpose = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getOriginalFileName());
            statement.setString(2, entity.getStorageFileName());
            statement.setLong(3, entity.getSize());
            statement.setString(4, entity.getType());
            statement.setString(5, entity.getPurpose());
            statement.setLong(6, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM file_info WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FileInfo> findFilesByPurpose(String purpose) {
        String sql = "SELECT * FROM file_info WHERE purpose = ?";
        List<FileInfo> files = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, purpose);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                files.add(mapRowToFileInfo(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return files;
    }

    @Override
    public void attachFileToEntity(Long fileId, String entityName, Long entityId) {
        String sql = "UPDATE " + entityName + " SET file_id = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, fileId);
            statement.setLong(2, entityId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void detachFileFromEntity(Long fileId, String entityName, Long entityId) {
        String sql = "UPDATE " + entityName + " SET file_id = NULL WHERE id = ? AND file_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, entityId);
            statement.setLong(2, fileId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private FileInfo mapRowToFileInfo(ResultSet resultSet) throws SQLException {
        return FileInfo.builder()
                .id(resultSet.getLong("id"))
                .originalFileName(resultSet.getString("original_file_name"))
                .storageFileName(resultSet.getString("storage_file_name"))
                .size(resultSet.getLong("size"))
                .type(resultSet.getString("type"))
                .purpose(resultSet.getString("purpose"))
                .build();
    }
}
