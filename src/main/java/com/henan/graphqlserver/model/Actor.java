package com.henan.graphqlserver.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Actor {
    private String id;
    private String name;
    private Integer birthYear;
} 