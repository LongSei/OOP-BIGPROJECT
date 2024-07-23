package com.example.Dictionary;

public abstract class DictionaryManagement {
    public abstract void importData(Dictionary dictionary);

    public abstract void exportData(Dictionary dictionary);

    // public void importDataFromDatabase() {
    //     ArrayList<ArrayList<String>> result = DataHelper.queryData("Words", new ArrayList<>(), "");
    //     Map<String, ArrayList<String>> inp = new HashMap<>();
    //     for (ArrayList<String> row : result) {
    //         String word_target = row.get(0);
    //         String word_explain = row.get(1);
    //         if (inp.containsKey(word_target)) {
    //             inp.get(word_target).add(word_explain);
    //         } else {
    //             ArrayList<String> temp = new ArrayList<>();
    //             temp.add(word_explain);
    //             inp.put(word_target, temp);
    //         }
    //     }
    // }
}
