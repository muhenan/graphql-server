package com.henan.graphqlserver.model;

import lombok.Data;

import java.util.List;

@Data
public class MovieInput {
    private String title;
    private Integer releaseYear;
    private String director;
    private String genre;
    private Float rating;
    private List<String> actorIds;
}