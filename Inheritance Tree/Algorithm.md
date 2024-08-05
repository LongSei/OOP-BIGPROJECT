# Algorithm Tree

## Trie Class
- **`Trie`**
  - Manages a Trie data structure with methods to insert, delete, search, and update words and their meanings.
  - **Inner Class: `TrieNode`**
    - Represents a node in the Trie with children and meanings.
  - **Methods:**
    - `insert(TrieNode node, String remain_wordTarget, List<String> wordExplain)`
    - `delete(TrieNode node, String remain_wordTarget)`
    - `searchMeaning(String wordTarget)`
    - `searchWord(String wordTarget)`
    - `startsWith(String prefix)`
    - `searchNode(String word)`
    - `importDictionary(Dictionary dictionary)`
    - `getSuggestions(String prefix)`
    - `collectWords(TrieNode node, String prefix, List<Word> suggestions)`
    - `removeMeaning(TrieNode node, String remain_wordTarget, String meaning)`
    - `updateWordTarget(String oldWordTarget, String newWordTarget)`
    - `updateWordMeaning(String wordTarget, String oldMeaning, String newMeaning)`
    - `printAllWords()`
    - `printAllWords(TrieNode node, String prefix)`
