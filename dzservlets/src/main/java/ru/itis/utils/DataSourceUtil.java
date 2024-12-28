package ru.itis.utils;

import org.apache.commons.dbcp2.BasicDataSource;
import javax.sql.DataSource;

public class DataSourceUtil {
    public static DataSource createDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/dbservlets1");
        dataSource.setUsername("postgres");
        dataSource.setPassword("------");
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setInitialSize(10);
        dataSource.setMaxTotal(50);
        return dataSource;
    }
}