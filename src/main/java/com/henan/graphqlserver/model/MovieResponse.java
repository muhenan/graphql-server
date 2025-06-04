package com.henan.graphqlserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieResponse {
    private boolean success;
    private String message;
    private Movie movie;
}