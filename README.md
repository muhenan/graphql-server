# GraphQL Movie Server

A GraphQL server built with Spring Boot and Netflix DGS (Domain Graph Service) that provides movie and actor information.

## Features

- Movie queries with details like title, director, genre, and rating
- Actor queries with basic information
- Movie mutations (create, update, delete)
- Movie filtering by title, genre, rating, and release year
- Sample data for demonstration purposes

## Technology Stack

- Java 21
- Spring Boot 3.4.6
- Netflix DGS 10.1.2
- Lombok for reducing boilerplate code

## Project Structure

```
src/main/
├── java/com/henan/graphqlserver/
│   ├── model/
│   │   ├── Movie.java
│   │   ├── MovieInput.java
│   │   ├── MovieResponse.java
│   │   ├── MovieFilter.java
│   │   └── Actor.java
│   └── datafetchers/
│       ├── MovieDatafetcher.java
│       └── ActorDatafetcher.java
└── resources/
    └── schema/
        └── schema.graphqls
```

## GraphQL Schema

The server provides the following main types:

### Movie
- id: ID!
- title: String!
- releaseYear: Int
- director: String
- genre: String
- rating: Float
- actors: [Actor]

### Actor
- id: ID!
- name: String!
- birthYear: Int

### MovieInput
- title: String!
- releaseYear: Int
- director: String
- genre: String
- rating: Float
- actorIds: [ID!]

### MovieResponse
- success: Boolean!
- message: String
- movie: Movie

### MovieFilter
- title: String
- genre: String
- minRating: Float
- maxRating: Float
- releaseYearFrom: Int
- releaseYearTo: Int

## Available Queries

```graphql
# Get all movies
query {
  movies {
    id
    title
    director
    genre
    rating
    actors {
      name
      birthYear
    }
  }
}

# Get movie by ID
query {
  movie(id: "1") {
    title
    director
    actors {
      name
    }
  }
}

# Get movies by genre
query {
  moviesByGenre(genre: "Drama") {
    title
    director
    rating
  }
}

# Search movies with filters
query {
  searchMovies(filter: {
    title: "shawshank",
    genre: "Drama",
    minRating: 9.0,
    releaseYearFrom: 1990
  }) {
    title
    director
    genre
    rating
    releaseYear
  }
}

# Get all actors
query {
  actors {
    id
    name
    birthYear
  }
}

# Get actor by ID
query {
  actor(id: "1") {
    name
    birthYear
  }
}
```

## Available Mutations

```graphql
# Create a new movie
mutation {
  createMovie(input: {
    title: "Inception",
    releaseYear: 2010,
    director: "Christopher Nolan",
    genre: "Sci-Fi",
    rating: 8.8,
    actorIds: ["1", "2"]
  }) {
    success
    message
    movie {
      id
      title
      director
      actors {
        name
      }
    }
  }
}

# Update a movie
mutation {
  updateMovie(
    id: "1",
    input: {
      title: "The Shawshank Redemption (Updated)",
      rating: 9.5
    }
  ) {
    success
    message
    movie {
      title
      rating
    }
  }
}

# Delete a movie
mutation {
  deleteMovie(id: "1") {
    success
    message
  }
}
```

## Sample Data

The server comes with sample data including:
- Movies: The Shawshank Redemption, The Godfather, The Dark Knight
- Actors: Tim Robbins, Morgan Freeman, Marlon Brando

## Getting Started

1. Clone the repository
2. Build the project:
   ```bash
   ./gradlew build
   ```
3. Run the application:
   ```bash
   ./gradlew bootRun
   ```
4. Access the GraphQL playground at `http://localhost:8080/graphiql`
