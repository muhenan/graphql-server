package com.henan.graphqlserver.datafetchers;

import com.henan.graphqlserver.model.Actor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActorDatafetcherTest {

    private ActorDatafetcher actorDatafetcher;

    @BeforeEach
    void setUp() {
        actorDatafetcher = new ActorDatafetcher();
    }

    @Test
    @DisplayName("Should return all actors")
    void shouldReturnAllActors() {
        List<Actor> actors = actorDatafetcher.actors();
        
        assertNotNull(actors);
        assertEquals(3, actors.size());
        assertTrue(actors.stream().anyMatch(a -> "Tim Robbins".equals(a.getName())));
        assertTrue(actors.stream().anyMatch(a -> "Morgan Freeman".equals(a.getName())));
        assertTrue(actors.stream().anyMatch(a -> "Marlon Brando".equals(a.getName())));
    }

    @Test
    @DisplayName("Should return actor by valid ID")
    void shouldReturnActorByValidId() {
        Actor actor = actorDatafetcher.actor("1");
        
        assertNotNull(actor);
        assertEquals("1", actor.getId());
        assertEquals("Tim Robbins", actor.getName());
        assertEquals(1958, actor.getBirthYear());
    }

    @Test
    @DisplayName("Should return specific actor details correctly")
    void shouldReturnSpecificActorDetailsCorrectly() {
        Actor actor2 = actorDatafetcher.actor("2");
        
        assertNotNull(actor2);
        assertEquals("2", actor2.getId());
        assertEquals("Morgan Freeman", actor2.getName());
        assertEquals(1937, actor2.getBirthYear());

        Actor actor3 = actorDatafetcher.actor("3");
        
        assertNotNull(actor3);
        assertEquals("3", actor3.getId());
        assertEquals("Marlon Brando", actor3.getName());
        assertEquals(1924, actor3.getBirthYear());
    }

    @Test
    @DisplayName("Should return null for invalid actor ID")
    void shouldReturnNullForInvalidId() {
        Actor actor = actorDatafetcher.actor("999");
        
        assertNull(actor);
    }

    @Test
    @DisplayName("Should return null for null ID")
    void shouldReturnNullForNullId() {
        Actor actor = actorDatafetcher.actor(null);
        
        assertNull(actor);
    }

    @Test
    @DisplayName("Should return null for empty ID")
    void shouldReturnNullForEmptyId() {
        Actor actor = actorDatafetcher.actor("");
        
        assertNull(actor);
    }

    @Test
    @DisplayName("Should maintain consistent actor data across multiple calls")
    void shouldMaintainConsistentActorDataAcrossMultipleCalls() {
        List<Actor> actors1 = actorDatafetcher.actors();
        List<Actor> actors2 = actorDatafetcher.actors();
        
        assertEquals(actors1.size(), actors2.size());
        for (int i = 0; i < actors1.size(); i++) {
            Actor actor1 = actors1.get(i);
            Actor actor2 = actors2.get(i);
            assertEquals(actor1.getId(), actor2.getId());
            assertEquals(actor1.getName(), actor2.getName());
            assertEquals(actor1.getBirthYear(), actor2.getBirthYear());
        }
    }

    @Test
    @DisplayName("Should verify all expected actors are present")
    void shouldVerifyAllExpectedActorsArePresent() {
        List<Actor> actors = actorDatafetcher.actors();
        
        Actor timRobbins = actors.stream()
                .filter(a -> "1".equals(a.getId()))
                .findFirst()
                .orElse(null);
        
        assertNotNull(timRobbins);
        assertEquals("Tim Robbins", timRobbins.getName());
        assertEquals(1958, timRobbins.getBirthYear());

        Actor morganFreeman = actors.stream()
                .filter(a -> "2".equals(a.getId()))
                .findFirst()
                .orElse(null);
        
        assertNotNull(morganFreeman);
        assertEquals("Morgan Freeman", morganFreeman.getName());
        assertEquals(1937, morganFreeman.getBirthYear());

        Actor marlonBrando = actors.stream()
                .filter(a -> "3".equals(a.getId()))
                .findFirst()
                .orElse(null);
        
        assertNotNull(marlonBrando);
        assertEquals("Marlon Brando", marlonBrando.getName());
        assertEquals(1924, marlonBrando.getBirthYear());
    }

    @Test
    @DisplayName("Should return actors in consistent order")
    void shouldReturnActorsInConsistentOrder() {
        List<Actor> actors1 = actorDatafetcher.actors();
        List<Actor> actors2 = actorDatafetcher.actors();
        
        for (int i = 0; i < actors1.size(); i++) {
            assertEquals(actors1.get(i).getId(), actors2.get(i).getId());
        }
    }
}