package com.project.task1.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtil {
    private static final List<List<Integer>> adjacencyMatrix = new ArrayList<>();

    private FileUtil() {}

    public static void addRowColumnAndInitialize() {
        List<Integer> newRow = new ArrayList<>();
        adjacencyMatrix.forEach(row -> newRow.add(0));
        adjacencyMatrix.add(newRow);
        adjacencyMatrix.forEach(row -> row.add(0));
    }

    public static void removeRowColumn(int index) {
        adjacencyMatrix.remove(index);
        adjacencyMatrix.forEach(row -> row.remove(index));
    }

    public static void updateMatrix(int row, int column, boolean isDirected, boolean arcAdded) {
        int newValue = arcAdded ? 1 : 0;
        adjacencyMatrix.get(row).set(column, newValue);
        if (!isDirected) {
            adjacencyMatrix.get(column).set(row, newValue);
        }
    }

    public static void writeToFile() throws IOException {
        try (FileWriter writer = new FileWriter("adjacency-matrix.txt")) {
            writer.write(adjacencyMatrix.size() + "\n");
            for (List<Integer> row : adjacencyMatrix) {
                writer.write(row.stream().map(String::valueOf).collect(Collectors.joining(" ")) + "\n");
            }
        }
    }

    public static String readFromFile() throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("adjacency-matrix.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
}
