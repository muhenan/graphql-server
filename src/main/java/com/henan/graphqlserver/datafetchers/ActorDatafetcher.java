package com.henan.graphqlserver.datafetchers;

import com.henan.graphqlserver.model.Actor;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
public class ActorDatafetcher {
    private final List<Actor> actors = new ArrayList<>();

    public ActorDatafetcher() {
        // Initialize with some sample data
        Actor actor1 = new Actor("1", "Tim Robbins", 1958);
        Actor actor2 = new Actor("2", "Morgan Freeman", 1937);
        Actor actor3 = new Actor("3", "Marlon Brando", 1924);

        actors.add(actor1);
        actors.add(actor2);
        actors.add(actor3);
    }

    @DgsQuery
    public Actor actor(@InputArgument String id) {
        return actors.stream()
                .filter(actor -> actor.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @DgsQuery
    public List<Actor> actors() {
        return actors;
    }
} 