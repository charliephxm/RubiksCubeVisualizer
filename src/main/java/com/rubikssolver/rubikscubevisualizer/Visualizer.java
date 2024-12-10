package com.rubikssolver.rubikscubevisualizer;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

public class Visualizer {
    private final Cube cube;
    private final GridPane cubeGrid;
    private final Map<Integer, int[]> facePositions;

    public static final int U = Cube.U;
    public static final int R = Cube.R;
    public static final int F = Cube.F;
    public static final int D = Cube.D;
    public static final int L = Cube.L;
    public static final int B = Cube.B;

    private static final int TOTAL_ROWS = 9;
    private static final int TOTAL_COLS = 12;
    private static final double RECT_SIZE = 20;
    private static final double RECT_GAP = 2;

    public Visualizer(Cube cube, Pane cubeArea) {
        this.cube = cube;
        this.cubeGrid = new GridPane();
        cubeArea.getChildren().add(cubeGrid);

        facePositions = new HashMap<>();
        facePositions.put(U, new int[]{0, 3});
        facePositions.put(L, new int[]{3, 0});
        facePositions.put(F, new int[]{3, 3});
        facePositions.put(R, new int[]{3, 6});
        facePositions.put(B, new int[]{3, 9});
        facePositions.put(D, new int[]{6, 3});

        initializeGrid();
    }

    private void initializeGrid() {
        cubeGrid.setHgap(RECT_GAP);
        cubeGrid.setVgap(RECT_GAP);

        for (int row = 0; row < TOTAL_ROWS; row++) {
            for (int col = 0; col < TOTAL_COLS; col++) {
                Pane pane = new Pane();
                pane.setPrefSize(RECT_SIZE, RECT_SIZE);
                cubeGrid.add(pane, col, row);
            }
        }
    }

    public void render() {
        cubeGrid.getChildren().clear();
        initializeGrid();

        String[][][] state = cube.getState();

        for (int face = 0; face < 6; face++) {
            int[] pos = facePositions.get(face);
            int baseRow = pos[0];
            int baseCol = pos[1];

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    Rectangle rect = new Rectangle(RECT_SIZE, RECT_SIZE, getColorFromCode(state[face][row][col]));
                    rect.setStroke(Color.BLACK);

                    int gridRow = baseRow + row;
                    int gridCol = baseCol + col;

                    cubeGrid.add(rect, gridCol, gridRow);
                }
            }
        }
    }

    private Color getColorFromCode(String colorCode) {
        return switch (colorCode) {
            case "W" -> Color.WHITE;
            case "Y" -> Color.YELLOW;
            case "R" -> Color.RED;
            case "O" -> Color.ORANGE;
            case "G" -> Color.GREEN;
            case "B" -> Color.BLUE;
            default -> Color.GRAY;
        };
    }
}