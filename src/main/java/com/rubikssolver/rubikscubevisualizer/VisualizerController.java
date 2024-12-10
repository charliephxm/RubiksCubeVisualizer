package com.rubikssolver.rubikscubevisualizer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.scene.control.Label;

import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.HashSet;

public class VisualizerController {

    @FXML
    private Pane cubeArea;

    @FXML
    private ComboBox<String> algorithmDropdown;

    @FXML
    private Button playButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button stepForwardButton;

    @FXML
    private Button stepBackwardButton;

    @FXML
    private Label currentMoveLabel;

    @FXML
    private Label stepCountLabel;

    @FXML
    private Label algorithmSequenceLabel;

    @FXML
    private ComboBox<String> categoryDropdown;

    @FXML
    private Label similarAlgorithmsLabel;

    private Cube cube;
    private Visualizer visualizer;
    private Algorithm currentAlgorithm;
    private int currentStep;

    private Timeline timeline;

    private final Stack<String> undoStack = new Stack<>();

    private MoveGraph moveGraph;

    @FXML
    private void initialize() {
        cube = new Cube();
        visualizer = new Visualizer(cube, cubeArea);
        visualizer.render();

        algorithmDropdown.setDisable(true);

        moveGraph = new MoveGraph();

        TreeNode root = AlgorithmFactory.getAlgorithmTree();
        List<String> categories = root.getChildren().stream()
                .map(TreeNode::getName)
                .toList();
        categoryDropdown.getItems().addAll(categories);

        playButton.setDisable(true);
        pauseButton.setDisable(true);
        stepForwardButton.setDisable(true);
        stepBackwardButton.setDisable(true);

        algorithmSequenceLabel.setText("Algorithm: ");
        currentMoveLabel.setText("Current Move: ");
        stepCountLabel.setText("Step: 0/0");

        categoryDropdown.setOnAction(e -> onCategorySelected());
        algorithmDropdown.setOnAction(e -> onAlgorithmSelected());

        playButton.setOnAction(e -> playAlgorithm());
        pauseButton.setOnAction(e -> pauseAlgorithm());
        stepForwardButton.setOnAction(e -> stepForward());
        stepBackwardButton.setOnAction(e -> stepBackward());
    }

    private void onCategorySelected() {
        String selectedCategory = categoryDropdown.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            algorithmDropdown.getItems().clear();

            TreeNode root = AlgorithmFactory.getAlgorithmTree();
            TreeNode categoryNode = root.getChildren().stream()
                    .filter(node -> node.getName().equals(selectedCategory))
                    .findFirst()
                    .orElse(null);

            if (categoryNode != null) {
                List<String> algorithms = categoryNode.getChildren().stream()
                        .map(TreeNode::getName)
                        .toList();
                algorithmDropdown.getItems().addAll(algorithms);
                algorithmDropdown.setDisable(false);
            } else {
                algorithmDropdown.setDisable(true);
            }

            algorithmDropdown.getSelectionModel().clearSelection();
            algorithmSequenceLabel.setText("Algorithm: ");
            currentMoveLabel.setText("Current Move: ");
            stepCountLabel.setText("Step: 0/0");
            currentAlgorithm = null;
            currentStep = 0;
            undoStack.clear();

            playButton.setDisable(true);
            pauseButton.setDisable(true);
            stepForwardButton.setDisable(true);
            stepBackwardButton.setDisable(true);

            similarAlgorithmsLabel.setText("Similar Algorithms: ");
        }
    }

    private void onAlgorithmSelected() {
        String selectedAlgorithm = algorithmDropdown.getSelectionModel().getSelectedItem();
        if (selectedAlgorithm != null) {
            currentAlgorithm = AlgorithmFactory.getAlgorithm(selectedAlgorithm);
            if (currentAlgorithm != null) {
                currentStep = 0;
                playButton.setDisable(false);
                pauseButton.setDisable(true);
                stepForwardButton.setDisable(false);
                stepBackwardButton.setDisable(true);
                cube.selectAlgorithm(selectedAlgorithm);
                visualizer.render();

                List<String> moves = currentAlgorithm.getMoves();
                StringBuilder algoSequence = new StringBuilder("Algorithm: ");
                for (int i = 0; i < moves.size(); i++) {
                    algoSequence.append(moves.get(i));
                    if (i < moves.size() - 1) {
                        algoSequence.append(" ");
                    }
                }
                algorithmSequenceLabel.setText(algoSequence.toString());

                currentMoveLabel.setText("Current Move: ");
                stepCountLabel.setText("Step: 0/" + moves.size());

                undoStack.clear();

                List<String> similar = getSimilarAlgorithms(selectedAlgorithm);
                if (!similar.isEmpty()) {
                    String similarText = String.join(", ", similar);
                    similarAlgorithmsLabel.setText("Similar Algorithms: " + similarText);
                } else {
                    similarAlgorithmsLabel.setText("Similar Algorithms: None");
                }
            }
        }
    }

    private void playAlgorithm() {
        if (currentAlgorithm == null) return;

        playButton.setDisable(true);
        pauseButton.setDisable(false);
        stepForwardButton.setDisable(true);
        stepBackwardButton.setDisable(true);

        List<String> moves = currentAlgorithm.getMoves();
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            if (currentStep < moves.size()) {
                String move = moves.get(currentStep);
                cube.applyMove(move);
                undoStack.push(move);
                visualizer.render();
                currentStep++;

                currentMoveLabel.setText("Current Move: " + move);
                stepCountLabel.setText("Step: " + currentStep + "/" + moves.size());

                stepBackwardButton.setDisable(false);

                if (currentStep >= moves.size()) {
                    timeline.stop();
                    playButton.setDisable(true);
                    pauseButton.setDisable(true);
                    stepForwardButton.setDisable(true);
                    stepBackwardButton.setDisable(false);
                }
            } else {
                timeline.stop();
                playButton.setDisable(false);
                pauseButton.setDisable(true);
                stepForwardButton.setDisable(true);
                stepBackwardButton.setDisable(false);
            }
        }));
        timeline.setCycleCount(moves.size());
        timeline.play();
    }

    private void pauseAlgorithm() {
        if (timeline != null) {
            timeline.pause();
            playButton.setDisable(false);
            pauseButton.setDisable(true);
            stepForwardButton.setDisable(false);
            stepBackwardButton.setDisable(false);
        }
    }

    private void stepForward() {
        if (currentAlgorithm == null || currentStep >= currentAlgorithm.getMoves().size()) return;

        String move = currentAlgorithm.getMoves().get(currentStep);
        cube.applyMove(move);
        undoStack.push(move);
        visualizer.render();
        currentStep++;

        currentMoveLabel.setText("Current Move: " + move);
        stepCountLabel.setText("Step: " + currentStep + "/" + currentAlgorithm.getMoves().size());

        stepBackwardButton.setDisable(false);
        if (currentStep >= currentAlgorithm.getMoves().size()) {
            stepForwardButton.setDisable(true);
        }
    }

    private void stepBackward() {
        if (undoStack.isEmpty()) return;

        String move = undoStack.pop();
        String inverseMove = cube.getInverseMove(move);
        cube.applyMove(inverseMove);
        visualizer.render();
        currentStep--;

        if (currentStep > 0) {
            String previousMove = currentAlgorithm.getMoves().get(currentStep - 1);
            currentMoveLabel.setText("Current Move: " + previousMove);
        } else {
            currentMoveLabel.setText("Current Move: ");
        }
        stepCountLabel.setText("Step: " + currentStep + "/" + currentAlgorithm.getMoves().size());

        stepForwardButton.setDisable(false);
        if (undoStack.isEmpty()) {
            stepBackwardButton.setDisable(true);
        }
    }

    /**
     * finds similar algorithms that share at least one move sequence with the given algorithm.
     *
     * @param algorithmName name of selected algorithm.
     * @return a list of similar algorithm names
     */
    public List<String> getSimilarAlgorithms(String algorithmName) {
        Algorithm currentAlgo = AlgorithmFactory.getAlgorithm(algorithmName);
        if (currentAlgo == null) return List.of();

        Set<String> similarAlgorithms = new HashSet<>();
        Graph<Move, DefaultEdge> graph = moveGraph.getGraph();

        Move algoMove = graph.vertexSet().stream()
                .filter(move -> move.getName().equals(algorithmName))
                .findFirst()
                .orElse(null);

        if (algoMove != null) {
            for (DefaultEdge edge : graph.outgoingEdgesOf(algoMove)) {
                Move connectedMove = graph.getEdgeTarget(edge);
                if (!connectedMove.getName().equals(algorithmName)) {
                    similarAlgorithms.add(connectedMove.getName());
                }
            }
        }

        return new ArrayList<>(similarAlgorithms);
    }
}
