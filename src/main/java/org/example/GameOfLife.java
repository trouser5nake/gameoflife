package org.example;

import java.io.*;

public class GameOfLife {

    private static final String PATH = "src/test/resources/";
    private static final char aliveCell = 'X';
    private static final char deadCell = 'O';
    private char[][] playField;
    private int width;
    private int height;
    private int ticksCount;

    public void game(String input, String output) {
        readInput(input);
        for (int i = 0; i < ticksCount; i++) {
            tick();
        }
        writeOutput(output);
    }

    public static void main(String[] args) {
        GameOfLife game = new GameOfLife();
        game.game("inputGlider.txt", "outputGlider.txt");
        game.game("inputGliderEasy.txt", "outputGliderEasy.txt");
        game.game("inputOscillator.txt", "outputOscillator.txt");
        game.game("inputOscillator2.txt", "outputOscillator2.txt");
        game.game("inputStable1.txt", "outputStable1.txt");
        game.game("inputStable2.txt", "outputStable2.txt");
    }

    private void readInput(String input) {
        try (BufferedReader br = new BufferedReader(new FileReader(PATH + input))) {
            String[] parameters = br.readLine().split(",");
            height = Integer.parseInt(parameters[0]);
            width = Integer.parseInt(parameters[1]);
            ticksCount = Integer.parseInt(parameters[2]);

            playField = new char[height][width];
            String inputLine;
            int numberOfLine = 0;
            while ((inputLine = br.readLine()) != null) {
                String cells = inputLine.replaceAll("\\s+", "");
                char[] lineSymbols = cells.toCharArray();
                for (int i = 0; i < cells.length(); i++) {
                    playField[numberOfLine][i] = lineSymbols[i];
                }
                numberOfLine++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeOutput(String output) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH + output))) {
            for (char[] line : playField) {
                StringBuilder outputLine = new StringBuilder();
                for (int i = 0; i < width; i++) {
                    char cell = line[i];
                    if (i == (width - 1)) {
                        outputLine.append(cell);
                    } else {
                        outputLine.append(cell).append(" ");
                    }
                }
                bw.write(outputLine.toString() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void tick() {
        char[][] tempField = new char[height][width];
        int neighbours;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char currentCell = playField[i][j];
                if (currentCell == aliveCell) {
                    neighbours = countNeighbours(i, j);
                    tempField[i][j] = switch (neighbours) {
                        case 2, 3 -> aliveCell;
                        default -> deadCell;
                    };
                } else {
                    neighbours = countNeighbours(i, j);
                    tempField[i][j] = (neighbours == 3) ? aliveCell : deadCell;
                }
            }
        }
        playField = tempField;
    }

    private int countNeighbours(int currentPositionY, int currentPositionX) {
        int neighboursCount = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int neighbourPositionY = (currentPositionY + i + height) % height;
                int neighbourPositionX = (currentPositionX + j + width) % width;
                if (playField[neighbourPositionY][neighbourPositionX] == aliveCell) {
                    neighboursCount++;
                }
            }
        }
        return neighboursCount;
    }
}
