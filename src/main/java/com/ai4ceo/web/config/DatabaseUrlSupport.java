package com.ai4ceo.web.config;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class DatabaseUrlSupport {

    private DatabaseUrlSupport() {}

    public static void applyFromEnv() {
        String explicit = firstEnv("SPRING_DATASOURCE_URL", "JDBC_DATABASE_URL");
        if (explicit != null) {
            System.setProperty("spring.datasource.url", explicit);
            return;
        }
        String raw = System.getenv("DATABASE_URL");
        if (raw == null || raw.isBlank()) {
            raw = readLocalDatabaseUrl();
        }
        if (raw == null || raw.isBlank()) {
            return;
        }
        if (raw.startsWith("jdbc:")) {
            System.setProperty("spring.datasource.url", raw);
            return;
        }
        URI uri = URI.create(raw);
        String userInfo = uri.getUserInfo();
        if (userInfo != null) {
            int colon = userInfo.indexOf(':');
            String user = colon >= 0 ? userInfo.substring(0, colon) : userInfo;
            String pass = colon >= 0 ? userInfo.substring(colon + 1) : "";
            System.setProperty("spring.datasource.username", decode(user));
            System.setProperty("spring.datasource.password", decode(pass));
        }
        int port = uri.getPort() > 0 ? uri.getPort() : 5432;
        String jdbc = "jdbc:postgresql://" + uri.getHost() + ":" + port + uri.getPath();
        String query = uri.getQuery();
        if (query == null || query.isBlank()) {
            jdbc += "?sslmode=require";
        } else {
            jdbc += "?" + query;
            if (!query.contains("sslmode")) {
                jdbc += "&sslmode=require";
            }
        }
        System.setProperty("spring.datasource.url", jdbc);
    }

    private static String readLocalDatabaseUrl() {
        Path path = Path.of("config", "render-database.url");
        if (!Files.isRegularFile(path)) {
            return null;
        }
        try {
            for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                    return trimmed;
                }
            }
        } catch (IOException ignored) {
            return null;
        }
        return null;
    }

    private static String firstEnv(String... keys) {
        for (String key : keys) {
            String value = System.getenv(key);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
