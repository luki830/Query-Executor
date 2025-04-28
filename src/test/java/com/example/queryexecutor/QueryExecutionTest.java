package com.example.queryexecutor;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class QueryExecutionTest {

    @Test
    void testSuccessfulQueryExecution() throws Exception {
        // Mockok létrehozása
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT * FROM users")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false); // Egy sor adat van
        when(resultSet.getString("name")).thenReturn("John Doe");

        // Végrehajtás
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

            rs.next();
            String result = rs.getString("name");

            // Ellenőrzés
            assertEquals("John Doe", result, "Result should match");
        }

        // Verifikáció
        verify(statement, times(1)).executeQuery("SELECT * FROM users");
    }

    @Test
    void testQueryExecutionFailure() throws Exception {
        // Mockolás hibás SQL-hez
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("INVALID SQL")).thenThrow(new RuntimeException("SQL Syntax Error"));

        // Ellenőrzés, hogy hibát dob-e
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.executeQuery("INVALID SQL");
            }
        });

        assertEquals("SQL Syntax Error", exception.getMessage());
    }
}