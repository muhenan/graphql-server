package com.henan.graphqlserver.datafetchers;

import com.henan.graphqlserver.model.Movie;
import com.henan.graphqlserver.model.Actor;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
public class MovieDatafetcher {
    // Sample data for demonstration
    private final List<Movie> movies = new ArrayList<>();

    public MovieDatafetcher() {
        // Initialize actors
        Actor actor1 = new Actor("1", "Tim Robbins", 1958);
        Actor actor2 = new Actor("2", "Morgan Freeman", 1937);
        Actor actor3 = new Actor("3", "Marlon Brando", 1924);

        // Initialize movies with actors
        Movie movie1 = new Movie("1", "The Shawshank Redemption", 1994, "Frank Darabont", "Drama", 9.3f, List.of(actor1, actor2));
        Movie movie2 = new Movie("2", "The Godfather", 1972, "Francis Ford Coppola", "Crime", 9.2f, List.of(actor3));
        Movie movie3 = new Movie("3", "The Dark Knight", 2008, "Christopher Nolan", "Action", 9.0f, new ArrayList<>());

        // Add movies to the list
        movies.add(movie1);
        movies.add(movie2);
        movies.add(movie3);
    }

    @DgsQuery
    public Movie movie(@InputArgument String id) {
        return movies.stream()
                .filter(movie -> movie.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @DgsQuery
    public List<Movie> movies() {
        return movies;
    }

    @DgsQuery
    public List<Movie> moviesByGenre(@InputArgument String genre) {
        return movies.stream()
                .filter(movie -> movie.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }
}
