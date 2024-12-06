package ru.itis.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Movie {
    private Long id;
    private String title;
    private String description;
    private int releaseYear;
}
