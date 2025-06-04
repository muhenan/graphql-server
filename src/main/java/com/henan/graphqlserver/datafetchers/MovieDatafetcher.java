package com.henan.graphqlserver.datafetchers;

import com.henan.graphqlserver.model.Movie;
import com.henan.graphqlserver.model.Actor;
import com.henan.graphqlserver.model.MovieInput;
import com.henan.graphqlserver.model.MovieResponse;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@DgsComponent
public class MovieDatafetcher {
    // Sample data for demonstration
    private final List<Movie> movies = new ArrayList<>();
    private final List<Actor> actors = new ArrayList<>();

    public MovieDatafetcher() {
        // Initialize sample actors
        Actor actor1 = new Actor("1", "Tim Robbins", 1958);
        Actor actor2 = new Actor("2", "Morgan Freeman", 1937);
        Actor actor3 = new Actor("3", "Marlon Brando", 1924);

        actors.add(actor1);
        actors.add(actor2);
        actors.add(actor3);

        // Initialize sample movies with actors
        Movie movie1 = new Movie("1", "The Shawshank Redemption", 1994, "Frank Darabont", "Drama", 9.3f, List.of(actor1, actor2));
        Movie movie2 = new Movie("2", "The Godfather", 1972, "Francis Ford Coppola", "Crime", 9.2f, List.of(actor3));
        Movie movie3 = new Movie("3", "The Dark Knight", 2008, "Christopher Nolan", "Action", 9.0f, new ArrayList<>());

        // Add movies to the list
        movies.add(movie1);
        movies.add(movie2);
        movies.add(movie3);
    }

    // Query methods
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

    // Mutation methods
    @DgsMutation
    public MovieResponse createMovie(@InputArgument MovieInput input) {
        try {
            Movie movie = new Movie();
            movie.setId(UUID.randomUUID().toString());
            movie.setTitle(input.getTitle());
            movie.setReleaseYear(input.getReleaseYear());
            movie.setDirector(input.getDirector());
            movie.setGenre(input.getGenre());
            movie.setRating(input.getRating());

            if (input.getActorIds() != null) {
                List<Actor> movieActors = actors.stream()
                    .filter(actor -> input.getActorIds().contains(actor.getId()))
                    .collect(Collectors.toList());
                movie.setActors(movieActors);
            }

            movies.add(movie);
            return new MovieResponse(true, "Movie created successfully", movie);
        } catch (Exception e) {
            return new MovieResponse(false, "Failed to create movie: " + e.getMessage(), null);
        }
    }

    @DgsMutation
    public MovieResponse updateMovie(@InputArgument String id, @InputArgument MovieInput input) {
        try {
            Movie movie = movies.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);

            if (movie == null) {
                return new MovieResponse(false, "Movie not found", null);
            }

            // Only update fields that are not null
            if (input.getTitle() != null) {
                movie.setTitle(input.getTitle());
            }
            if (input.getReleaseYear() != null) {
                movie.setReleaseYear(input.getReleaseYear());
            }
            if (input.getDirector() != null) {
                movie.setDirector(input.getDirector());
            }
            if (input.getGenre() != null) {
                movie.setGenre(input.getGenre());
            }
            if (input.getRating() != null) {
                movie.setRating(input.getRating());
            }
            if (input.getActorIds() != null) {
                List<Actor> movieActors = actors.stream()
                    .filter(actor -> input.getActorIds().contains(actor.getId()))
                    .collect(Collectors.toList());
                movie.setActors(movieActors);
            }

            return new MovieResponse(true, "Movie updated successfully", movie);
        } catch (Exception e) {
            return new MovieResponse(false, "Failed to update movie: " + e.getMessage(), null);
        }
    }

    @DgsMutation
    public MovieResponse deleteMovie(@InputArgument String id) {
        try {
            boolean removed = movies.removeIf(movie -> movie.getId().equals(id));
            if (removed) {
                return new MovieResponse(true, "Movie deleted successfully", null);
            } else {
                return new MovieResponse(false, "Movie not found", null);
            }
        } catch (Exception e) {
            return new MovieResponse(false, "Failed to delete movie: " + e.getMessage(), null);
        }
    }
}
