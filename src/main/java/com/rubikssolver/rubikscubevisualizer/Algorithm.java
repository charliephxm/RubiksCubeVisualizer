package com.rubikssolver.rubikscubevisualizer;

import java.util.ArrayList;
import java.util.List;

public class Algorithm {
    private final String name;
    private final List<String> moves;

    public Algorithm(String name, List<String> moves) {
        this.name = name;
        this.moves = new ArrayList<>(moves);
    }

    public String getName() {
        return name;
    }

    public List<String> getMoves() {
        return moves;
    }

    @Override
    public String toString() {
        return name;
    }
}