package com.henan.graphqlserver.datafetchers;

import com.henan.graphqlserver.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieDatafetcherTest {

    private MovieDatafetcher movieDatafetcher;

    @BeforeEach
    void setUp() {
        movieDatafetcher = new MovieDatafetcher();
    }

    @Test
    @DisplayName("Should return all movies")
    void shouldReturnAllMovies() {
        List<Movie> movies = movieDatafetcher.movies();
        
        assertNotNull(movies);
        assertEquals(3, movies.size());
        assertTrue(movies.stream().anyMatch(m -> "The Shawshank Redemption".equals(m.getTitle())));
        assertTrue(movies.stream().anyMatch(m -> "The Godfather".equals(m.getTitle())));
        assertTrue(movies.stream().anyMatch(m -> "The Dark Knight".equals(m.getTitle())));
    }

    @Test
    @DisplayName("Should return movie by valid ID")
    void shouldReturnMovieByValidId() {
        Movie movie = movieDatafetcher.movie("1");
        
        assertNotNull(movie);
        assertEquals("1", movie.getId());
        assertEquals("The Shawshank Redemption", movie.getTitle());
        assertEquals(1994, movie.getReleaseYear());
        assertEquals("Frank Darabont", movie.getDirector());
        assertEquals("Drama", movie.getGenre());
        assertEquals(9.3f, movie.getRating());
    }

    @Test
    @DisplayName("Should return null for invalid movie ID")
    void shouldReturnNullForInvalidId() {
        Movie movie = movieDatafetcher.movie("999");
        
        assertNull(movie);
    }

    @Test
    @DisplayName("Should return movies by genre")
    void shouldReturnMoviesByGenre() {
        List<Movie> dramaMovies = movieDatafetcher.moviesByGenre("Drama");
        
        assertNotNull(dramaMovies);
        assertEquals(1, dramaMovies.size());
        assertEquals("The Shawshank Redemption", dramaMovies.get(0).getTitle());
    }

    @Test
    @DisplayName("Should return empty list for non-existent genre")
    void shouldReturnEmptyListForNonExistentGenre() {
        List<Movie> movies = movieDatafetcher.moviesByGenre("Horror");
        
        assertNotNull(movies);
        assertTrue(movies.isEmpty());
    }

    @Test
    @DisplayName("Should create movie successfully")
    void shouldCreateMovieSuccessfully() {
        MovieInput input = new MovieInput();
        input.setTitle("Inception");
        input.setReleaseYear(2010);
        input.setDirector("Christopher Nolan");
        input.setGenre("Sci-Fi");
        input.setRating(8.8f);

        MovieResponse response = movieDatafetcher.createMovie(input);

        assertTrue(response.isSuccess());
        assertEquals("Movie created successfully", response.getMessage());
        assertNotNull(response.getMovie());
        assertEquals("Inception", response.getMovie().getTitle());
        assertEquals(2010, response.getMovie().getReleaseYear());
        assertEquals("Christopher Nolan", response.getMovie().getDirector());
        assertEquals("Sci-Fi", response.getMovie().getGenre());
        assertEquals(8.8f, response.getMovie().getRating());
        assertNotNull(response.getMovie().getId());

        List<Movie> movies = movieDatafetcher.movies();
        assertEquals(4, movies.size());
    }

    @Test
    @DisplayName("Should update existing movie successfully")
    void shouldUpdateExistingMovieSuccessfully() {
        MovieInput input = new MovieInput();
        input.setTitle("The Shawshank Redemption - Updated");
        input.setRating(9.5f);

        MovieResponse response = movieDatafetcher.updateMovie("1", input);

        assertTrue(response.isSuccess());
        assertEquals("Movie updated successfully", response.getMessage());
        assertNotNull(response.getMovie());
        assertEquals("The Shawshank Redemption - Updated", response.getMovie().getTitle());
        assertEquals(9.5f, response.getMovie().getRating());
        assertEquals(1994, response.getMovie().getReleaseYear());
        assertEquals("Frank Darabont", response.getMovie().getDirector());
    }

    @Test
    @DisplayName("Should return error when updating non-existent movie")
    void shouldReturnErrorWhenUpdatingNonExistentMovie() {
        MovieInput input = new MovieInput();
        input.setTitle("Non-existent Movie");

        MovieResponse response = movieDatafetcher.updateMovie("999", input);

        assertFalse(response.isSuccess());
        assertEquals("Movie not found", response.getMessage());
        assertNull(response.getMovie());
    }

    @Test
    @DisplayName("Should delete existing movie successfully")
    void shouldDeleteExistingMovieSuccessfully() {
        MovieResponse response = movieDatafetcher.deleteMovie("1");

        assertTrue(response.isSuccess());
        assertEquals("Movie deleted successfully", response.getMessage());
        assertNull(response.getMovie());

        List<Movie> movies = movieDatafetcher.movies();
        assertEquals(2, movies.size());
        assertFalse(movies.stream().anyMatch(m -> "1".equals(m.getId())));
    }

    @Test
    @DisplayName("Should return error when deleting non-existent movie")
    void shouldReturnErrorWhenDeletingNonExistentMovie() {
        MovieResponse response = movieDatafetcher.deleteMovie("999");

        assertFalse(response.isSuccess());
        assertEquals("Movie not found", response.getMessage());
        assertNull(response.getMovie());
    }

    @Test
    @DisplayName("Should search movies with title filter")
    void shouldSearchMoviesWithTitleFilter() {
        MovieFilter filter = new MovieFilter();
        filter.setTitle("shawshank");

        List<Movie> movies = movieDatafetcher.searchMovies(filter);

        assertNotNull(movies);
        assertEquals(1, movies.size());
        assertEquals("The Shawshank Redemption", movies.get(0).getTitle());
    }

    @Test
    @DisplayName("Should search movies with genre filter")
    void shouldSearchMoviesWithGenreFilter() {
        MovieFilter filter = new MovieFilter();
        filter.setGenre("Drama");

        List<Movie> movies = movieDatafetcher.searchMovies(filter);

        assertNotNull(movies);
        assertEquals(1, movies.size());
        assertEquals("The Shawshank Redemption", movies.get(0).getTitle());
    }

    @Test
    @DisplayName("Should search movies with rating range filter")
    void shouldSearchMoviesWithRatingRangeFilter() {
        MovieFilter filter = new MovieFilter();
        filter.setMinRating(9.0f);
        filter.setMaxRating(9.5f);

        List<Movie> movies = movieDatafetcher.searchMovies(filter);

        assertNotNull(movies);
        assertEquals(3, movies.size());
    }

    @Test
    @DisplayName("Should search movies with release year range filter")
    void shouldSearchMoviesWithReleaseYearRangeFilter() {
        MovieFilter filter = new MovieFilter();
        filter.setReleaseYearFrom(1990);
        filter.setReleaseYearTo(2000);

        List<Movie> movies = movieDatafetcher.searchMovies(filter);

        assertNotNull(movies);
        assertEquals(1, movies.size());
        assertEquals("The Shawshank Redemption", movies.get(0).getTitle());
    }

    @Test
    @DisplayName("Should search movies with multiple filters")
    void shouldSearchMoviesWithMultipleFilters() {
        MovieFilter filter = new MovieFilter();
        filter.setGenre("Drama");
        filter.setMinRating(9.0f);
        filter.setReleaseYearFrom(1990);

        List<Movie> movies = movieDatafetcher.searchMovies(filter);

        assertNotNull(movies);
        assertEquals(1, movies.size());
        assertEquals("The Shawshank Redemption", movies.get(0).getTitle());
    }

    @Test
    @DisplayName("Should return all movies when filter is null")
    void shouldReturnAllMoviesWhenFilterIsNull() {
        List<Movie> movies = movieDatafetcher.searchMovies(null);

        assertNotNull(movies);
        assertEquals(3, movies.size());
    }

    @Test
    @DisplayName("Should return empty list when no movies match filter")
    void shouldReturnEmptyListWhenNoMoviesMatchFilter() {
        MovieFilter filter = new MovieFilter();
        filter.setGenre("Horror");
        filter.setMinRating(10.0f);

        List<Movie> movies = movieDatafetcher.searchMovies(filter);

        assertNotNull(movies);
        assertTrue(movies.isEmpty());
    }
}