package com.rubikssolver.rubikscubevisualizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlgorithmFactory {
    private static final Map<String, Algorithm> algorithms = new HashMap<>();
    private static final TreeNode root = new TreeNode("Algorithms");

    static {
        TreeNode oll = new TreeNode("OLL");
        TreeNode pll = new TreeNode("PLL");

        root.addChild(oll);
        root.addChild(pll);

        List<Algorithm> ollAlgorithms = List.of(
                new Algorithm("1", List.of("R", "U", "U", "R'", "R'", "F", "R", "F'", "U", "U", "R'", "F", "R", "F'")),
                new Algorithm("2", List.of("F", "R", "U", "R'", "U'", "F'", "U", "U", "F", "U", "R", "U'", "R'", "F'")),
                new Algorithm("3", List.of("F", "U", "U", "F", "R'", "F'", "R", "U", "R", "U", "R'", "U", "F'")),
                new Algorithm("4", List.of("F'", "U", "U", "F'", "L", "F", "L'", "U'", "L'", "U'", "L", "U'", "F")),
                new Algorithm("5", List.of("F", "R", "U", "R'", "U'", "F'", "U'", "F", "R", "U", "R'", "U'", "F'")),
                new Algorithm("6", List.of("F", "U'", "R", "R", "D", "R'", "U'", "R", "D'", "R", "R", "U", "F'")),
                new Algorithm("7", List.of("L'", "U", "U", "L", "U", "U", "L", "F'", "L'", "F")),
                new Algorithm("8", List.of("R", "U", "U", "R'", "U", "U", "R'", "F", "R", "F'")),
                new Algorithm("9", List.of("U", "R'", "U'", "R", "F", "R'", "F'", "U", "F", "R", "F'")),
                new Algorithm("10", List.of("R", "U", "R'", "U", "R'", "F", "R", "F'", "R", "U", "U", "R'")),
                new Algorithm("11", List.of("U", "U", "R", "U", "R'", "U'", "R'", "F", "R", "F'", "L'", "U'", "L", "U", "L", "F'", "L'", "F")),
                new Algorithm("12", List.of("F", "R", "U", "R'", "U'", "F'", "U", "F", "R", "U", "R'", "U'", "F'")),
                new Algorithm("13", List.of("F", "U", "R", "U", "U", "R'", "U'", "R", "U", "R'", "F'")),
                new Algorithm("14", List.of("R'", "F", "R", "U", "R'", "F'", "R", "F", "U'", "F'")),
                new Algorithm("15", List.of("F", "U", "R", "U'", "R", "D", "R'", "U'", "R", "D'", "R", "R", "U'", "R", "U", "R'", "F'")),
                new Algorithm("16", List.of("L", "F", "L'", "R", "U", "R'", "U'", "L", "F'", "L'")),
                new Algorithm("17", List.of("R", "U", "R'", "U", "R'", "F", "R", "F'", "U", "U", "R'", "F", "R", "F'")),
                new Algorithm("18", List.of("F", "R'", "F'", "R", "U", "R", "U'", "R'", "U", "F", "R", "U", "R'", "U'", "F'")),
                new Algorithm("19", List.of("R'", "U", "U", "F", "R", "U", "R'", "U'", "F", "F", "U", "U", "F", "R")),
                new Algorithm("20", List.of("R", "U", "R'", "U'", "R'", "F", "R", "F'", "R", "U", "U", "R", "R", "F", "R", "F'", "R", "U", "U", "R'"))
        );

        List<Algorithm> pllAlgorithms = List.of(
                new Algorithm("F-Perm", List.of("R'", "U'", "F'", "R", "U", "R'", "U'", "R'", "F", "R", "R", "U'", "R'", "U'", "R", "U", "R'", "U", "R")),
                new Algorithm("Ja-Perm", List.of("R'", "U", "L'", "U", "U", "R", "U'", "R'", "U", "U", "R", "L")),
                new Algorithm("Jb-Perm", List.of("R", "U", "R'", "F'", "R", "U", "R'", "U'", "R'", "F", "R", "R", "U'", "R'", "U'")),
                new Algorithm("T-Perm", List.of("R", "U", "R'", "U'", "R'", "F", "R", "R", "U'", "R'", "U'", "R", "U", "R'", "F'")),
                new Algorithm("Y-Perm", List.of("F", "R", "U'", "R'", "U'", "R", "U", "R'", "F'", "R", "U", "R'", "U'", "R'", "F", "R", "F'")),
                new Algorithm("Aa-Perm", List.of("R'", "F", "R'", "B", "B", "R", "F'", "R'", "B", "B", "R", "R")),
                new Algorithm("Ga-Perm", List.of("R", "R", "U", "R'", "U", "R'", "U'", "R", "U'", "R", "R", "D", "U'", "R'", "U", "R", "D'")),
                new Algorithm("Na-Perm", List.of("F'", "R", "U", "R'", "U'", "R'", "F", "R", "R", "F", "U'", "R'", "U'", "R", "U", "F'", "R'")),
                new Algorithm("Ra-Perm", List.of("L", "U", "U", "L'", "U", "U", "L", "F'", "L'", "U'", "L", "U", "L", "F", "L", "L")),
                new Algorithm("Ua-Perm", List.of("R", "U", "R'", "U", "R'", "U'", "R", "R", "U'", "R'", "U", "R'", "U", "R"))
        );

        ollAlgorithms.forEach(algorithm -> {
            algorithms.put(algorithm.getName(), algorithm);
            oll.addChild(new TreeNode(algorithm.getName()));
        });

        pllAlgorithms.forEach(algorithm -> {
            algorithms.put(algorithm.getName(), algorithm);
            pll.addChild(new TreeNode(algorithm.getName()));
        });
    }

    public static Algorithm getAlgorithm(String name) {
        return algorithms.get(name);
    }

    public static List<String> getAllAlgorithmNames() {
        return List.copyOf(algorithms.keySet());
    }

    public static TreeNode getAlgorithmTree() {
        return root;
    }
}