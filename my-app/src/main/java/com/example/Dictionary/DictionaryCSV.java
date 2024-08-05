package com.example.Dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DictionaryCSV extends DictionaryManagement {
    private final File file = new File("src/main/resources/data.csv");

    /**
     * Import data from a CSV file.
     * The CSV file should have 2 columns: Word and Meaning.
     * Each row should have a word and its meaning separated by a comma.
     * @param dictionary The dictionary to import data to.
     */
    @Override
    public void importData(Dictionary dictionary) {
        if (!file.exists()) {
            System.err.println("CSV file not found: " + file.getAbsolutePath());
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 2) {
                    String word = values[0];
                    List<String> meaning = List.of(values[1]);
                    dictionary.addWord(new Word(word, meaning));
                } else {
                    System.err.println("Skipping invalid line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    /**
     * Export data to a CSV file.
     * The CSV file will have 2 columns: Word and Meaning.
     * Each row will have a word and its meaning separated by a comma.
     * @param dictionary The dictionary to export data from.
     */
    @Override
    public void exportData(Dictionary dictionary) {
        List<List<String>> data = new ArrayList<>();
        for (Word word : dictionary.getAllWords()) {
            for (String word_explain : word.getWordExplain()) {
                List<String> row = new ArrayList<>();
                row.add(word.getWordTarget());
                row.add(word_explain);
                data.add(row);
            }
        }
        writeData(file, data);
    }

    private void writeData(File file, List<List<String>> data) {
        try (FileWriter writer = new FileWriter(file)) {
            for (List<String> row : data) {
                writer.write(String.join(",", row));
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }
}
