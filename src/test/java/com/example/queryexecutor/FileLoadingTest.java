package com.example.queryexecutor;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileLoadingTest {

    @Test
    void testFileExists() throws Exception {
        // Feltételezve, hogy a fájl létezik
        String filePath = "./src/main/resources/queries/get_user_data.sql";
        assertTrue(Files.exists(Paths.get(filePath)), "File should exist");
    }

    @Test
    void testFileNotFound() {
        // Olyan fájlra mutat, ami nem létezik
        String filePath = "./src/main/resources/queries/nonexistent.sql";

        // Ellenőrizzük, hogy NoSuchFileException keletkezik
        Exception exception = assertThrows(java.nio.file.NoSuchFileException.class, () -> {
            Files.readString(Paths.get(filePath));
        });

        // Ellenőrizzük a pontos fájlnevet a hibaüzenetben
        String expectedMessage = "nonexistent.sql";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Hibaüzenet nem tartalmazza a fájl nevét.");
    }
}