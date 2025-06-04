# GraphQL Movie Server

A GraphQL server built with Spring Boot and Netflix DGS (Domain Graph Service) that provides movie and actor information.

## Features

- Movie queries with details like title, director, genre, and rating
- Actor queries with basic information
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