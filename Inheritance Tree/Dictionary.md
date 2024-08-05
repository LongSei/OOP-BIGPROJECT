# Dictionary Tree

## DictionaryManagement
- **Purpose**: Abstract class for managing dictionary data.
- **Methods**:
  - `importData(Dictionary dictionary)`
  - `exportData(Dictionary dictionary)`

### DictionaryCommandLine
- **Inheritance**: Inherits from `DictionaryManagement`
- **Purpose**: Command-line interface for interacting with the dictionary.
- **Methods**:
  - `importData(Dictionary dictionary)`
  - `exportData(Dictionary dictionary)`
  - `textWrapping(String text, int width)`
  - `tableCreating(Map<String, List<String>> data, List<Integer> columnWidth)`

### DictionaryCSV
- **Inheritance**: Inherits from `DictionaryManagement`
- **Purpose**: Handles importing and exporting dictionary data to and from a CSV file.
- **Methods**:
  - `importData(Dictionary dictionary)`
  - `exportData(Dictionary dictionary)`
  - `writeData(File file, List<List<String>> data)`

### DictionaryDatabase
- **Inheritance**: Inherits from `DictionaryManagement`
- **Purpose**: Manages dictionary data with a database.
- **Methods**:
  - `importData(Dictionary dictionary)`
  - `exportData(Dictionary dictionary)`

## Dictionary
- **Purpose**: Manages a collection of `Word` objects.
- **Methods**:
  - `addWord(Word word)`
  - `removeWord(Word word)`
  - `removeMeaning(Word word, String meaning)`
  - `updateWord(String oldWord, String newWord)`
  - `updateMeaning(String word, String oldMeaning, String newMeaning)`
  - `getWordByIndex(int index)`
  - `getWordByTarget(String target)`
  - `getSize()`
  - `getAllWords()`

## Word
- **Purpose**: Represents a word with its target and meanings.
- **Methods**:
  - `getWordTarget()`
  - `setWordTarget(String wordTarget)`
  - `getWordExplain()`
  - `setWordExplain(List<String> wordExplain)`
  - `addWordExplain(String explanation)`
  - `removeWordExplain(String explanation)`
  - `toString()`
