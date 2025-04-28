package com.example.queryexecutor;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/execute-query")
class QueryController {

    @Value("${queries.directory:./queries/}")
    private String queriesDirectory;

    private final DataSource dataSource;

    public QueryController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final Logger logger = LoggerFactory.getLogger(QueryController.class);

    @GetMapping
    public ResponseEntity<?> executeQuery(@RequestParam String query_identifier) {
        long startTime = System.currentTimeMillis();
        String queryFilePath = queriesDirectory + query_identifier + ".sql";

        try {
            // Load query from file
            ClassPathResource resource = new ClassPathResource(queryFilePath);
            //if (!Files.exists(resource.toString())) {
            if (!resource.exists()) {
                logger.error("Query file not found: {}", query_identifier);
                return ResponseEntity.status(404).body(Map.of(
                        "success", false,
                        "error", "Query file not found" ,
                        "code", 404
                ));
            }

            //String query = Files.readString(Paths.get(queryFilePath));
            String query = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            // Execute query
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                List<Map<String, Object>> results = new ArrayList<>();
                int columnCount = resultSet.getMetaData().getColumnCount();

                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
                    }
                    results.add(row);
                }

                long executionTime = System.currentTimeMillis() - startTime;

                logger.info("Query executed: {}, Execution time: {} ms, Status: {}", query_identifier, executionTime, 200);

                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "data", results,
                        "execution_time_ms", executionTime
                ));
            }
        } catch (Exception e) {

            logger.error("Error executing query: {}, Error: {}", query_identifier, e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "error", e.getMessage(),
                    "code", 500
            ));
        }
    }
}