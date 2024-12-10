package com.rubikssolver.rubikscubevisualizer;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.List;

public class MoveGraph {
    private static final int MIN_SHARED_SUBSEQUENCE_LENGTH = 5;
    private final Graph<Move, DefaultEdge> graph;

    public MoveGraph() {
        graph = new SimpleDirectedGraph<>(DefaultEdge.class);
        initializeGraph();
    }

    private void initializeGraph() {

        Move one = new Move("One", "R U2 R' R' F R F' U2 R' F R F'", "OLL");
        Move two = new Move("Two", "F R U R' U' F' U2 F U R U' R' F'", "OLL");
        Move three = new Move("Three", "F U2 F R' F' R U R U R' U F'", "OLL");
        Move four = new Move("Four", "F' U2 F' L F L' U' L' U' L U' F", "OLL");
        Move five = new Move("Five", "F R U R' U' F' U' F R U R' U' F'", "OLL");
        Move six = new Move("Six", "F U' R2 D R' U' R D' R2 U F'", "OLL");
        Move seven = new Move("Seven", "L' U2 L U2 L F' L' F", "OLL");
        Move eight = new Move("Eight", "R U2 R' U2 R' F R F'", "OLL");
        Move nine = new Move("Nine", "U R' U' R F R' F' U F R F'", "OLL");
        Move ten = new Move("Ten", "R U R' U R' F R F' R U2 R'", "OLL");
        Move eleven = new Move("Eleven", "U2 R U R' U' R' F R F' L' U' L U L F' L' F", "OLL");
        Move twelve = new Move("Twelve", "F R U R' U' F' U F R U R' U' F'", "OLL");
        Move thirteen = new Move("Thirteen", "F U R U2 R' U' R U R' F'", "OLL");
        Move fourteen = new Move("Fourteen", "R' F R U R' F' R F U' F'", "OLL");
        Move fifteen = new Move("Fifteen", "F U R U' R D R' U' R D' R2 U' R U R' F'", "OLL");
        Move sixteen = new Move("Sixteen", "L F L' R U R' U' L F' L'", "OLL");
        Move seventeen = new Move("Seventeen", "R U R' U R' F R F' U2 R' F R F'", "OLL");
        Move eighteen = new Move("Eighteen", "F R' F' R U R U' R' U F R U R' U' F'", "OLL");
        Move nineteen = new Move("Nineteen", "R' U2 F R U R' U' F2 U2 F R", "OLL");
        Move twenty = new Move("Twenty", "R U R' U' R' F R F' R U2 R2 F R F' R U2 R'", "OLL");

        Move fPerm = new Move("F-Perm", "R' U' F' R U R' U' R' F R2 U' R' U' R U R' U R", "PLL");
        Move jaPerm = new Move("Ja-Perm", "R' U L' U2 R U' R' U2 R L", "PLL");
        Move jbPerm = new Move("Jb-Perm", "R U R' F' R U R' U' R' F R2 U' R'", "PLL");
        Move tPerm = new Move("T-Perm", "R U R' U' R' F R2 U' R' U' R U R' F'", "PLL");
        Move yPerm = new Move("Y-Perm", "F R U' R' U' R U R F' R U R' U' R' F R F'", "PLL");
        Move aaPerm = new Move("Aa-Perm", "R' F R' B2 R F' R' B2 R2", "PLL");
        Move gaPerm = new Move("Ga-Perm", "R2 U R' U R' U' R U' R2 D U' R' U R D'", "PLL");
        Move naPerm = new Move("Na-Perm", "F' R U R' U' R' F R2 F U' R' U' R U F' R'", "PLL");
        Move raPerm = new Move("Ra-Perm", "L U2 L' U2 L F' L' U' L U L F L2", "PLL");
        Move uaPerm = new Move("Ua-Perm", "R U R' U R' U' R2 U' R' U R' U R", "PLL");

        List<Move> allMoves = List.of(fPerm, jaPerm, jbPerm, tPerm, yPerm, aaPerm, gaPerm, naPerm, raPerm, uaPerm, one, two, three, four, five, six,
                seven, eight, nine, ten, eleven, twelve, thirteen, fourteen, fifteen, sixteen, seventeen, eighteen,
                nineteen, twenty
        );
        allMoves.forEach(graph::addVertex);

        for (int i = 0; i < allMoves.size(); i++) {
            for (int j = i + 1; j < allMoves.size(); j++) {
                Move moveA = allMoves.get(i);
                Move moveB = allMoves.get(j);

                if (moveA.getCategory().equals(moveB.getCategory()) &&
                        hasMinimumSharedSubsequence(moveA.getSequence(), moveB.getSequence())) {
                    graph.addEdge(moveA, moveB);
                    graph.addEdge(moveB, moveA);
                }
            }
        }
    }

    /**
     * Checks if two sequences share a common subsequence of at least the minimum length.
     *
     * @param sequenceA The first sequence.
     * @param sequenceB The second sequence.
     * @return true if they share a common subsequence of sufficient length; false otherwise.
     */
    private boolean hasMinimumSharedSubsequence(String sequenceA, String sequenceB) {
        String[] movesA = sequenceA.split(" ");
        String[] movesB = sequenceB.split(" ");

        for (int startA = 0; startA <= movesA.length - MIN_SHARED_SUBSEQUENCE_LENGTH; startA++) {
            for (int startB = 0; startB <= movesB.length - MIN_SHARED_SUBSEQUENCE_LENGTH; startB++) {
                int matchLength = 0;

                while (startA + matchLength < movesA.length &&
                        startB + matchLength < movesB.length &&
                        movesA[startA + matchLength].equals(movesB[startB + matchLength])) {
                    matchLength++;
                    if (matchLength >= MIN_SHARED_SUBSEQUENCE_LENGTH) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Gets the graph containing moves and their relationships.
     *
     * @return The graph of moves and edges.
     */
    public Graph<Move, DefaultEdge> getGraph() {
        return graph;
    }
}