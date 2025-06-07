# GraphQL Movie Server

A GraphQL server built with Spring Boot and Netflix DGS (Domain Graph Service) that provides movie and actor information.

## Features

- Movie queries with details like title, director, genre, and rating
- Actor queries with basic information
- Movie mutations (create, update, delete)
- Movie filtering by title, genre, rating, and release year
- Comprehensive unit test coverage
- Sample data for demonstration purposes

## Technology Stack

- Java 21
- Spring Boot 3.4.6
- Netflix DGS 10.1.2
- Lombok for reducing boilerplate code

## Project Structure

```
src/
├── main/
│   ├── java/com/henan/graphqlserver/
│   │   ├── model/
│   │   │   ├── Movie.java
│   │   │   ├── MovieInput.java
│   │   │   ├── MovieResponse.java
│   │   │   ├── MovieFilter.java
│   │   │   └── Actor.java
│   │   └── datafetchers/
│   │       ├── MovieDatafetcher.java
│   │       └── ActorDatafetcher.java
│   └── resources/
│       └── schema/
│           └── schema.graphqls
└── test/
    └── java/com/henan/graphqlserver/
        └── datafetchers/
            ├── MovieDatafetcherTest.java
            └── ActorDatafetcherTest.java
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

## Testing

The project includes comprehensive unit tests built with JUnit 5 to ensure code quality and reliability.

### Test Coverage

- **MovieDatafetcher Tests**: Complete test suite covering all query and mutation operations
  - Query operations: `movies()`, `movie(id)`, `moviesByGenre()`, `searchMovies()`
  - Mutation operations: `createMovie()`, `updateMovie()`, `deleteMovie()`
  - Edge cases: Invalid IDs, null filters, empty results
  - Filter combinations: Title, genre, rating range, release year range

- **ActorDatafetcher Tests**: Comprehensive test suite for actor operations
  - Query operations: `actors()`, `actor(id)`
  - Edge cases: Invalid IDs, null/empty IDs
  - Data consistency and validation across multiple calls

### Running Tests

```bash
# Run all tests
./gradlew test

# Run tests with more verbose output
./gradlew test --info

# Run specific test class
./gradlew test --tests MovieDatafetcherTest
./gradlew test --tests ActorDatafetcherTest
```

### Test Structure

```
src/test/
└── java/com/henan/graphqlserver/
    └── datafetchers/
        ├── MovieDatafetcherTest.java
        └── ActorDatafetcherTest.java
```

The tests use JUnit 5 assertions and follow best practices for unit testing, including:
- Proper setup and teardown with `@BeforeEach`
- Descriptive test names with `@DisplayName`
- Comprehensive assertions for all expected outcomes
- Edge case validation

## Future Work

### Relay-Style Connection Pattern
The server could be enhanced with Relay-style connection pattern, which provides a standardized way to handle pagination and relationships in GraphQL. This pattern would include:

1. **Connection Types**
   - `MovieConnection`: Contains edges, pageInfo, and totalCount
   - `MovieEdge`: Contains a node (the actual movie) and a cursor
   - `PageInfo`: Contains pagination metadata (hasNextPage, hasPreviousPage, etc.)

2. **Type Definitions**
   ```graphql
   # Connection type for paginated movie results
   type MovieConnection {
       edges: [MovieEdge!]!    # List of edges containing movies
       pageInfo: PageInfo!     # Pagination metadata
       totalCount: Int!        # Total number of movies
   }

   # Edge type containing a movie and its cursor
   type MovieEdge {
       node: Movie!            # The actual movie object
       cursor: String!         # Cursor for pagination
   }

   # Pagination metadata
   type PageInfo {
       hasNextPage: Boolean!           # Whether there are more pages
       hasPreviousPage: Boolean!       # Whether there are previous pages
       startCursor: String             # Cursor for the first item
       endCursor: String               # Cursor for the last item
   }
   ```

3. **Benefits**
   - Cursor-based pagination for better performance
   - Consistent structure across the API
   - Rich metadata for UI development
   - Support for bi-directional pagination

4. **Example Implementation**
   ```graphql
   # Query with pagination
   query {
     movies(first: 2) {
       edges {
         node {
           id
           title
           director
         }
         cursor
       }
       pageInfo {
         hasNextPage
         hasPreviousPage
         startCursor
         endCursor
       }
       totalCount
     }
   }
   ```

5. **Planned Features**
   - Cursor-based pagination for all list queries
   - Support for bi-directional navigation
   - Integration with filtering system
   - Performance optimizations for large datasets
