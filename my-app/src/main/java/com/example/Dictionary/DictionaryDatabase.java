package com.example.Dictionary;

import com.example.Database.DataHelper;
import java.util.ArrayList;
import java.util.List;

public class DictionaryDatabase extends DictionaryManagement {
    /**
     * Import data from the database.
     * The words will be added to the dictionary.
     * @param dictionary The dictionary to import data to.
     */
    @Override
    public void importData(Dictionary dictionary) {
        try {
            DataHelper dataHelper = new DataHelper();
            List<List<String>> data = dataHelper.queryData("Words", null);
            for (List<String> row : data) {
                String word_target = row.get(1).toLowerCase();
                String word_explain = row.get(2).toLowerCase();

                List<String> word_explain_list = new ArrayList<>();
                word_explain_list.add(word_explain);

                Word word = new Word(word_target, word_explain_list);
                dictionary.addWord(word);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    /**
     * Export data to the database.
     * The words will be exported to the database.
     * @param dictionary The dictionary to export data from.
     */
    @Override
    public void exportData(Dictionary dictionary) {
        try {
            DataHelper dataHelper = new DataHelper();
            dataHelper.dropTable("Words");
            dataHelper.createTable();
            dataHelper.clearTable("Words");

            for (Word word : dictionary.getAllWords()) {
                for (String word_explain : word.getWordExplain()) {
                    List<String> colums = new ArrayList<>();
                    List<String> values = new ArrayList<>();

                    colums.add("word_target");
                    colums.add("word_explain");

                    values.add(word.getWordTarget().toLowerCase());
                    values.add(word_explain.toLowerCase());

                    dataHelper.insertData("Words", colums, values);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
}
