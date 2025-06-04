package com.henan.graphqlserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MovieFilter {
    private String title;
    private String genre;
    private Float minRating;
    private Float maxRating;
    private Integer releaseYearFrom;
    private Integer releaseYearTo;
}
