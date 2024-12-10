package com.rubikssolver.rubikscubevisualizer;

public class Move {
    private final String name;
    private final String sequence;
    private final String category;

    public Move(String name, String sequence, String category) {
        this.name = name;
        this.sequence = sequence;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getSequence() {
        return sequence;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return name;
    }
}
