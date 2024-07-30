package com.example.Dictionary;

import com.example.Database.DataHelper;
import java.util.ArrayList;
import java.util.List;

public class DictionaryDatabase extends DictionaryManagement {
    @Override
    public void importData(Dictionary dictionary) {
        DataHelper dataHelper = new DataHelper();
        List<List<String>> data = dataHelper.queryData("Words", null);
        for (List<String> row : data) {
            String word_target = row.get(1);
            String word_explain = row.get(2);

            List<String> word_explain_list = new ArrayList<>();
            word_explain_list.add(word_explain);

            Word word = new Word(word_target, word_explain_list);
            dictionary.addWord(word);
        }
    };

    @Override
    public void exportData(Dictionary dictionary) {
        return;
    };
}
