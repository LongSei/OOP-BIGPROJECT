# Database Tree

## DataCondition Class
- **`DataCondition`**
  - Provides methods for creating various types of SQL conditions used in queries.
    - `mergeCondition(String condition1, String condition2, String typeLogical)`
    - `comparisonCondition(String leftParam, String rightParam, String typeComparison)`
    - `betweenCondition(String param, String leftParam, String rightParam)`
    - `inCondition(String param, ArrayList<String> values)`
    - `likeCondition(String param, String value)`
    - `isNullCondition(String param)`
    - `isNotNullCondition(String param)`
    - `distinctCondition(String param)`

## DataHelper Class
- **`DataHelper`**
  - Provides methods for interacting with a SQLite database, including creating tables, inserting, updating, deleting, and querying data.
    - **Constants:**
      - `DATABASE_URL`
      - `CREATE_WORD_TABLE`
      - `CREATE_TEXT_TO_SPEECH_REQUESTS_TABLE`
    - `getConnection()`
    - `closeConnection(Connection conn)`
    - `createTable()`
    - `dropTable(String table)`
    - `insertData(String table, List<String> columns, List<String> values)`
    - `updateData(String table, List<String> columns, List<String> values)`
    - `updateData(String table, List<String> columns, List<String> values, String condition)`
    - `deleteData(String table)`
    - `deleteData(String table, String condition)`
    - `queryData(String table, List<String> columns)`
    - `queryData(String table, List<String> columns, String condition)`
    - `clearTable(String table)`
    - `getAllColumns(String table)`
