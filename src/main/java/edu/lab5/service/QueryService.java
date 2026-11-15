package edu.lab5.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class QueryService {

    public static final List<String> EXAMPLES = List.of(
            "SELECT r.id, c.plate, c.brand, c.model, r.description, r.price, r.repair_date, r.status\n"
                    + "FROM repairs r JOIN cars c ON c.id = r.car_id\n"
                    + "WHERE r.status <> 'Виконано'\n"
                    + "ORDER BY r.repair_date",
            "SELECT c.plate, c.brand, c.model, COUNT(r.id) AS repairs_count, COALESCE(SUM(r.price), 0) AS total\n"
                    + "FROM cars c LEFT JOIN repairs r ON r.car_id = c.id\n"
                    + "GROUP BY c.id, c.plate, c.brand, c.model\n"
                    + "ORDER BY total DESC",
            "SELECT * FROM cars WHERE year < 2015 ORDER BY year");

    public record Result(List<String> columns, List<List<String>> rows) {
    }

    private final JdbcTemplate jdbc;

    public QueryService(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
        this.jdbc.setMaxRows(500);
    }

    @Transactional(readOnly = true)
    public Result run(String sql) {
        String query = sql == null ? "" : sql.trim();
        if (query.endsWith(";")) {
            query = query.substring(0, query.length() - 1).trim();
        }
        String lower = query.toLowerCase(Locale.ROOT);
        if (!(lower.startsWith("select") || lower.startsWith("with")) || query.contains(";")) {
            throw new IllegalArgumentException("Дозволено лише один запит SELECT");
        }
        return jdbc.query(query, (ResultSetExtractor<Result>) rs -> {
            ResultSetMetaData md = rs.getMetaData();
            int n = md.getColumnCount();
            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                columns.add(md.getColumnLabel(i));
            }
            List<List<String>> rows = new ArrayList<>();
            while (rs.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= n; i++) {
                    String value = rs.getString(i);
                    row.add(value == null ? "NULL" : value);
                }
                rows.add(row);
            }
            return new Result(columns, rows);
        });
    }
}
