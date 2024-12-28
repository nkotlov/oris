package ru.itis.models;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {
    private Long id;
    private String title;
    private String description;
    private Integer releaseYear;
    private Long directorId;
    private Long fileId;
}
