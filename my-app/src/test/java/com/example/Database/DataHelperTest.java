package com.example.Database;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataHelperTest {

    @BeforeEach
    public void setupDatabase() {
        DataHelper.createTable();
    }

    @AfterEach
    public void teardownDatabase() {
        try (Connection conn = DataHelper.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Words;");
            stmt.execute("DROP TABLE IF EXISTS Translations;");
            stmt.execute("DROP TABLE IF EXISTS TextToSpeechRequests;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConnection() {
        try {
            Connection conn = DataHelper.getConnection();
            assertNotNull(conn);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred while testing connection");
        }
    }

    @Test
    public void testCreateTable() {
        try {
            DataHelper.createTable();
            try (Connection conn = DataHelper.getConnection();
                 Statement stmt = conn.createStatement()) {
                assertNotNull(conn);

                assertNotNull(stmt);

                ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Words';");
                assertTrue(rs.next());
                rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='TextToSpeechRequests';");
                assertTrue(rs.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred while testing table creation");
        }
    }

    @Test
    public void testInsertData() {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("word_target");
        columns.add("word_explain");

        ArrayList<String> values = new ArrayList<>();
        values.add("hello");
        values.add("xin chào");

        DataHelper.insertData("Words", columns, values);

        ArrayList<ArrayList<String>> result = DataHelper.queryData("Words", columns, "word_target='hello'");
        assertEquals(1, result.size());
        assertEquals("hello", result.get(0).get(0));
        assertEquals("xin chào", result.get(0).get(1));
    }

    @Test
    public void testUpdateData() {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("word_target");
        columns.add("word_explain");

        ArrayList<String> values = new ArrayList<>();
        values.add("hello");
        values.add("xin chào");

        DataHelper.insertData("Words", columns, values);

        ArrayList<String> newColumns = new ArrayList<>();
        newColumns.add("word_explain");

        ArrayList<String> newValues = new ArrayList<>();
        newValues.add("chào bạn");

        DataHelper.updateData("Words", newColumns, newValues, "word_target='hello'");

        ArrayList<ArrayList<String>> result = DataHelper.queryData("Words", newColumns, "word_target='hello'");
        assertEquals(1, result.size());
        assertEquals("chào bạn", result.get(0).get(0));
    }

    @Test
    public void testDeleteData() {
        DataHelper.deleteData("Words", "word_target='hello'");

        ArrayList<String> columns = new ArrayList<>();
        columns.add("word_target");
        columns.add("word_explain");

        ArrayList<ArrayList<String>> result = DataHelper.queryData("Words", columns, "word_target='hello'");
        assertEquals(0, result.size());
    }

    @Test
    public void testQueryData() {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("word_target");
        columns.add("word_explain");

        ArrayList<String> values = new ArrayList<>();
        values.add("hello");
        values.add("xin chào");

        DataHelper.insertData("Words", columns, values);

        ArrayList<ArrayList<String>> result = DataHelper.queryData("Words", columns, "word_target='hello'");
        assertEquals(1, result.size());
        assertEquals("hello", result.get(0).get(0));
        assertEquals("xin chào", result.get(0).get(1));
    }
}
