package com.project.task1.util;

import java.io.FileWriter;
import java.io.IOException;
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

    public static void updateMatrix(int row, int column) {
        adjacencyMatrix.get(row).set(column, 1);
        adjacencyMatrix.get(column).set(row, 1);
    }

    public static void writeToFile() throws IOException {
        try (FileWriter fileWriter = new FileWriter("adjacency-matrix.txt")) {
            fileWriter.write(adjacencyMatrix.size() + "\n");
            for (List<Integer> row : adjacencyMatrix) {
                fileWriter.write(row.stream().map(String::valueOf).collect(Collectors.joining(" ")) + "\n");
            }
        }
    }
}
