package com.example.Dictionary;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DictionaryCommandLine extends DictionaryManagement {
    /**
     * Import data from user input.
     * The user will input the number of words and then input the word and its meaning.
     * The words will be added to the dictionary.
     * @param dictionary The dictionary to import data to.
     */
    @Override
    public void importData(Dictionary dictionary) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of words: ");
        int n = scanner.nextInt();
        scanner.nextLine();
        Map<String, ArrayList<String>> inp = new HashMap<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter the word: ");
            String word_target = scanner.nextLine();
            System.out.print("Enter the meaning: ");
            String word_explain = scanner.nextLine();
            if (inp.containsKey(word_target)) {
                inp.get(word_target).add(word_explain);
            } else {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(word_explain);
                inp.put(word_target, temp);
            }
        }
        scanner.close();
        for (Map.Entry<String, ArrayList<String>> entry : inp.entrySet()) {
            Word word = new Word(entry.getKey(), entry.getValue());
            dictionary.addWord(word);
        }
    }

    /** 
     * Show all words in the dictionary in table format.
     * The table will have 2 columns: Word and Meaning.
     * The width of the columns will be adjusted to fit the content.
     * @param dictionary The dictionary to be displayed.
     * @return The table as a String.
     */
    @Override
    public void exportData(Dictionary dictionary) {
        Map<String, List<String>> dataSplitByColumn = new HashMap<>();
        for (Word word : dictionary.getAllWords()) {
            dataSplitByColumn.put(word.getWordTarget(), word.getWordExplain());
        }

        List<Integer> columnWidth = new ArrayList<>();
        columnWidth.add(10);
        columnWidth.add(15);

        String table = tableCreating(dataSplitByColumn, columnWidth);
        System.out.println(table);
    }

    private String textWrapping(String text, int width) {
        String wrappedText = "";
        String[] words = text.split(" ");
        String line = "";

        for (String word : words) {
            if (line.length() + word.length() + 1 > width) {
                String thisLine = line.toString().trim();
                int remainingSpaces = width - thisLine.length();
                thisLine += " ".repeat(remainingSpaces);
                wrappedText += thisLine + "\n";
                line = "";
            }
            line += word + " ";
        }

        if (line.length() > 0) {
            String thisLine = line.toString().trim();
            int remainingSpaces = width - thisLine.length();
            thisLine += " ".repeat(remainingSpaces);
            wrappedText += thisLine + "\n";
        }

        return wrappedText.toString();
    }

    private String tableCreating(Map<String, List<String>> data, List<Integer> columnWidth) {
        String table = "+" + "-".repeat(columnWidth.get(0)) + "+" + "-".repeat(columnWidth.get(1)) + "+\n";
        for (Map.Entry<String, List<String>> entry : data.entrySet()) {
            String word = entry.getKey();
            List<String> meanings = entry.getValue();

            String wordColumn = textWrapping(word, columnWidth.get(0));
            
            for (int i = 0; i < meanings.size(); i++) {
                String meaning = meanings.get(i);
                String meaningColumn = textWrapping(meaning, columnWidth.get(1));
                table += "|" + wordColumn + "|" +  meaningColumn + "|\n";
            }
            table += "+" + "-".repeat(columnWidth.get(0)) + "+" + "-".repeat(columnWidth.get(1)) + "+\n";
        }
        return table;
    }
}
