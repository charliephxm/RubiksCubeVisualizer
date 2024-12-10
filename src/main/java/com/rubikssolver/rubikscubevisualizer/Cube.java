package com.rubikssolver.rubikscubevisualizer;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Cube {
    public static final int U = 0;
    public static final int R = 1;
    public static final int F = 2;
    public static final int D = 3;
    public static final int L = 4;
    public static final int B = 5;

    private String[][][] state;

    private final Map<String, String[][][]> algorithmPreStates = new HashMap<>();

    public Cube() {
        reset();
        initializeAlgorithmPreStates();
    }

    private Cube(boolean initializePreStates) {
        reset();
        if (initializePreStates) {
            initializeAlgorithmPreStates();
        }
    }

    public void selectAlgorithm(String algorithmName) {
        if (algorithmPreStates.containsKey(algorithmName)) {
            String[][][] preState = algorithmPreStates.get(algorithmName);
            setState(preState);
        } else {
            throw new IllegalArgumentException("Algorithm not found: " + algorithmName);
        }
    }

    private void setState(String[][][] newState) {
        for (int face = 0; face < 6; face++) {
            for (int row = 0; row < 3; row++) {
                System.arraycopy(newState[face][row], 0, state[face][row], 0, 3);
            }
        }
    }

    public String[][][] getStateCopy() {
        String[][][] copy = new String[6][3][3];
        for (int face = 0; face < 6; face++) {
            for (int row = 0; row < 3; row++) {
                copy[face][row] = state[face][row].clone();
            }
        }
        return copy;
    }

    private void initializeAlgorithmPreStates() {
        List<String> algorithmNames = AlgorithmFactory.getAllAlgorithmNames();
        for (String algoName : algorithmNames) {
            Algorithm algo = AlgorithmFactory.getAlgorithm(algoName);
            if (algo != null) {
                Cube preStateCube = new Cube(false);

                List<String> inverseMoves = new ArrayList<>();
                List<String> moves = algo.getMoves();
                for (int i = moves.size() - 1; i >= 0; i--) {
                    inverseMoves.add(getInverseMove(moves.get(i)));
                }

                for (String move : inverseMoves) {
                    preStateCube.applyMove(move);
                }

                algorithmPreStates.put(algoName, preStateCube.getStateCopy());
            }
        }
    }

    public void reset() {
        String[] colors = {"Y", "O", "G", "W", "R", "B"};
        state = new String[6][3][3];

        for (int face = 0; face < 6; face++) {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    state[face][row][col] = colors[face];
                }
            }
        }
    }

    public void applyMove(String move) {
        boolean clockwise = true;
        String moveFace = move;

        if (move.endsWith("'")) {
            clockwise = false;
            moveFace = move.substring(0, move.length() - 1);
        }

        int faceIndex = mapMoveToLogicalOrientation(moveFace.charAt(0));

        rotateFace(faceIndex, clockwise);
    }

    public String getInverseMove(String move) {
        if (move.endsWith("'")) {
            return move.substring(0, move.length() - 1);
        } else {
            return move + "'";
        }
    }

    private int mapMoveToLogicalOrientation(char faceChar) {
        return switch (faceChar) {
            case 'U' -> U;
            case 'R' -> R;
            case 'F' -> F;
            case 'D' -> D;
            case 'L' -> L;
            case 'B' -> B;
            default -> throw new IllegalArgumentException("Invalid move: " + faceChar);
        };
    }

    public void rotateFace(int faceIndex, boolean clockwise) {
        String[][] face = state[faceIndex];
        String[][] rotatedFace = new String[3][3];

        if (clockwise) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rotatedFace[j][2 - i] = face[i][j];
                }
            }
        } else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rotatedFace[2 - j][i] = face[i][j];
                }
            }
        }

        state[faceIndex] = rotatedFace;

        rotateEdges(faceIndex, clockwise);
    }

    private void rotateEdges(int faceIndex, boolean clockwise) {
        switch (faceIndex) {
            case F:
                rotateFront(clockwise);
                break;
            case B:
                rotateBack(clockwise);
                break;
            case U:
                rotateUp(clockwise);
                break;
            case D:
                rotateDown(clockwise);
                break;
            case R:
                rotateRight(clockwise);
                break;
            case L:
                rotateLeft(clockwise);
                break;
            default:
                throw new IllegalArgumentException("Invalid face index: " + faceIndex);
        }
    }

    private void rotateFront(boolean clockwise) {
        String[] uBottom = state[U][2].clone();
        String[] rLeftCol = {state[R][0][0], state[R][1][0], state[R][2][0]};
        String[] dTop = state[D][0].clone();
        String[] lRightCol = {state[L][0][2], state[L][1][2], state[L][2][2]};

        if (clockwise) {
            for (int i = 0; i < 3; i++) {
                state[R][i][0] = uBottom[i];
            }

            for (int i = 0; i < 3; i++) {
                state[D][0][i] = rLeftCol[2 - i];
            }

            for (int i = 0; i < 3; i++) {
                state[L][i][2] = dTop[i];
            }

            for (int i = 0; i < 3; i++) {
                state[U][2][i] = lRightCol[2 - i];
            }
        } else {
            System.arraycopy(rLeftCol, 0, state[U][2], 0, 3);

            for (int i = 0; i < 3; i++) {
                state[R][i][0] = dTop[2 - i];
            }

            System.arraycopy(lRightCol, 0, state[D][0], 0, 3);

            for (int i = 0; i < 3; i++) {
                state[L][i][2] = uBottom[2 - i];
            }
        }
    }

    private void rotateBack(boolean clockwise) {
        String[] uTop = state[U][0].clone();
        String[] lLeftCol = {state[L][0][0], state[L][1][0], state[L][2][0]};
        String[] dBottom = state[D][2].clone();
        String[] rRightCol = {state[R][0][2], state[R][1][2], state[R][2][2]};

        if (clockwise) {
            for (int i = 0; i < 3; i++) {
                state[L][i][0] = uTop[i];
            }

            for (int i = 0; i < 3; i++) {
                state[D][2][i] = lLeftCol[2 - i];
            }

            for (int i = 0; i < 3; i++) {
                state[R][i][2] = dBottom[i];
            }

            for (int i = 0; i < 3; i++) {
                state[U][0][i] = rRightCol[2 - i];
            }
        } else {
            for (int i = 0; i < 3; i++) {
                state[U][0][i] = lLeftCol[2 - i];
            }

            for (int i = 0; i < 3; i++) {
                state[L][i][0] = dBottom[i];
            }

            for (int i = 0; i < 3; i++) {
                state[D][2][i] = rRightCol[2 - i];
            }

            for (int i = 0; i < 3; i++) {
                state[R][i][2] = uTop[i];
            }
        }
    }

    private void rotateUp(boolean clockwise) {
        String[] fTop = state[F][0].clone();
        String[] rTop = state[R][0].clone();
        String[] bTop = state[B][0].clone();
        String[] lTop = state[L][0].clone();

        if (clockwise) {
            System.arraycopy(rTop, 0, state[F][0], 0, 3);
            System.arraycopy(bTop, 0, state[R][0], 0, 3);
            System.arraycopy(lTop, 0, state[B][0], 0, 3);
            System.arraycopy(fTop, 0, state[L][0], 0, 3);
        } else {
            System.arraycopy(fTop, 0, state[R][0], 0, 3);
            System.arraycopy(rTop, 0, state[B][0], 0, 3);
            System.arraycopy(bTop, 0, state[L][0], 0, 3);
            System.arraycopy(lTop, 0, state[F][0], 0, 3);
        }
    }

    private void rotateDown(boolean clockwise) {
        String[] fBottom = state[F][2].clone();
        String[] lBottom = state[L][2].clone();
        String[] bBottom = state[B][2].clone();
        String[] rBottom = state[R][2].clone();

        if (clockwise) {
            System.arraycopy(lBottom, 0, state[F][2], 0, 3);
            System.arraycopy(bBottom, 0, state[L][2], 0, 3);
            System.arraycopy(rBottom, 0, state[B][2], 0, 3);
            System.arraycopy(fBottom, 0, state[R][2], 0, 3);
        } else {
            System.arraycopy(fBottom, 0, state[L][2], 0, 3);
            System.arraycopy(lBottom, 0, state[B][2], 0, 3);
            System.arraycopy(bBottom, 0, state[R][2], 0, 3);
            System.arraycopy(rBottom, 0, state[F][2], 0, 3);
        }
    }

    private void rotateRight(boolean clockwise) {
        String[] uRightCol = {state[U][0][2], state[U][1][2], state[U][2][2]};
        String[] fRightCol = {state[F][0][2], state[F][1][2], state[F][2][2]};
        String[] dRightCol = {state[D][0][2], state[D][1][2], state[D][2][2]};
        String[] bLeftCol  = {state[B][0][0], state[B][1][0], state[B][2][0]};

        if (clockwise) {
            for (int i = 0; i < 3; i++) state[B][i][0] = uRightCol[2 - i];
            for (int i = 0; i < 3; i++) state[D][i][2] = bLeftCol[2 - i];
            for (int i = 0; i < 3; i++) state[F][i][2] = dRightCol[i];
            for (int i = 0; i < 3; i++) state[U][i][2] = fRightCol[i];
        } else {
            for (int i = 0; i < 3; i++) state[U][i][2] = bLeftCol[2 - i];
            for (int i = 0; i < 3; i++) state[B][i][0] = dRightCol[2 - i];
            for (int i = 0; i < 3; i++) state[D][i][2] = fRightCol[i];
            for (int i = 0; i < 3; i++) state[F][i][2] = uRightCol[i];
        }
    }

    private void rotateLeft(boolean clockwise) {
        String[] uLeftCol = {state[U][0][0], state[U][1][0], state[U][2][0]};
        String[] bRightCol = {state[B][0][2], state[B][1][2], state[B][2][2]};
        String[] dLeftCol = {state[D][0][0], state[D][1][0], state[D][2][0]};
        String[] fLeftCol = {state[F][0][0], state[F][1][0], state[F][2][0]};

        if (clockwise) {
            for (int i = 0; i < 3; i++) state[U][i][0] = bRightCol[2 - i];
            for (int i = 0; i < 3; i++) state[B][i][2] = dLeftCol[2 - i];
            for (int i = 0; i < 3; i++) state[D][i][0] = fLeftCol[i];
            for (int i = 0; i < 3; i++) state[F][i][0] = uLeftCol[i];
        } else {
            for (int i = 0; i < 3; i++) state[B][i][2] = uLeftCol[2 - i];
            for (int i = 0; i < 3; i++) state[D][i][0] = bRightCol[2 - i];
            for (int i = 0; i < 3; i++) state[F][i][0] = dLeftCol[i];
            for (int i = 0; i < 3; i++) state[U][i][0] = fLeftCol[i];
        }
    }
    public String[][][] getState() {
        return state;
    }
}