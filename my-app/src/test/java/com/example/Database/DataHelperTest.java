package com.example.Database;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataHelperTest {
    private DataHelper dataHelper = new DataHelper();

    @BeforeEach
    public void setupDatabase() {
        dataHelper.createTable();
    }

    @AfterEach
    public void teardownDatabase() {
        try (Connection conn = dataHelper.getConnection();
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
            Connection conn = dataHelper.getConnection();
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
            dataHelper.createTable();
            try (Connection conn = dataHelper.getConnection();
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
        List<String> columns = new ArrayList<>();
        columns.add("word_target");
        columns.add("word_explain");

        List<String> values = new ArrayList<>();
        values.add("hello");
        values.add("xin chào");

        dataHelper.insertData("Words", columns, values);

        List<List<String>> result = dataHelper.queryData("Words", columns, "word_target='hello'");
        assertEquals(1, result.size());
        assertEquals("hello", result.get(0).get(0));
        assertEquals("xin chào", result.get(0).get(1));
    }

    @Test
    public void testUpdateData() {
        List<String> columns = new ArrayList<>();
        columns.add("word_target");
        columns.add("word_explain");

        List<String> values = new ArrayList<>();
        values.add("hello");
        values.add("xin chào");

        dataHelper.insertData("Words", columns, values);

        List<String> newColumns = new ArrayList<>();
        newColumns.add("word_explain");

        List<String> newValues = new ArrayList<>();
        newValues.add("chào bạn");

        dataHelper.updateData("Words", newColumns, newValues, "word_target='hello'");

        List<List<String>> result = dataHelper.queryData("Words", newColumns, "word_target='hello'");
        assertEquals(1, result.size());
        assertEquals("chào bạn", result.get(0).get(0));
    }

    @Test
    public void testDeleteData() {
        dataHelper.deleteData("Words", "word_target='hello'");

        List<String> columns = new ArrayList<>();
        columns.add("word_target");
        columns.add("word_explain");

        List<List<String>> result = dataHelper.queryData("Words", columns, "word_target='hello'");
        assertEquals(0, result.size());
    }

    @Test
    public void testQueryData() {
        List<String> columns = new ArrayList<>();
        columns.add("word_target");
        columns.add("word_explain");

        List<String> values = new ArrayList<>();
        values.add("hello");
        values.add("xin chào");

        dataHelper.insertData("Words", columns, values);

        List<List<String>> result = dataHelper.queryData("Words", columns, "word_target='hello'");
        assertEquals(1, result.size());
        assertEquals("hello", result.get(0).get(0));
        assertEquals("xin chào", result.get(0).get(1));
    }
}
