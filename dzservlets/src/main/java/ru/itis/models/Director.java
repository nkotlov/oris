package ru.itis.models;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Director {
    private Long id;
    private String name;
    private String bio;
    private LocalDate birthDate;
    private Long photoFileId;
}
