package com.henan.graphqlserver.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    private String id;
    private String title;
    private Integer releaseYear;
    private String director;
    private String genre;
    private Float rating;
    private List<Actor> actors;
} 