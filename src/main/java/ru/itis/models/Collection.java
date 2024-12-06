package ru.itis.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Collection {
    private Long id;
    private String name;
    private String description;
    private User user;
}
