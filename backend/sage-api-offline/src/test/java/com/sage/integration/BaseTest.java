//package com.sage.integration;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class BaseTest {
//
//    @Autowired
//    protected TestRestTemplate restTemplate;
//
//    protected Connection connection;
//
//    public BaseTest() {
//        try {
//            this.connection = DriverManager.getConnection(
//                    "jdbc:postgresql://172.27.193.206:5432/sage",
//                    "postgres",
//                    "postgres"
//            );
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to connect to the database", e);
//        }
//    }
//
//    private List<String> tables() {
//        List<String> tables = new ArrayList<>();
//        tables.add("resident");
//        tables.add("organization");
//        tables.add("alarm");
//        tables.add("control_resident");
//        return tables;
//    }
//
//    private void cleanDatabase() throws SQLException {
//        for (String table : tables()) {
//            connection.prepareStatement("DELETE FROM " + table).executeUpdate();
//        }
//    }
//
//    @BeforeEach
//    @Transactional
//    public void setUp() throws SQLException {
//        cleanDatabase();
//    }
//
//    protected List<Map<String, Object>> list(String table) {
//        try {
//            ResultSet resultSet = connection.prepareStatement("SELECT * FROM " + table).executeQuery();
//            if (resultSet == null) {
//                return new ArrayList<>();
//            }
//
//            List<Map<String, Object>> residentsMap = new ArrayList<>();
//
//            try {
//                while (resultSet.next()) {
//                    ResultSetMetaData metaData = resultSet.getMetaData();
//                    int columnCount = metaData.getColumnCount();
//
//                    Map<String, Object> residentMap = new HashMap<>();
//                    for (int i = 1; i <= columnCount; i++) {
//                        String columnName = metaData.getColumnName(i);
//                        Object value = resultSet.getObject(i);
//                        residentMap.put(columnName, value);
//                    }
//                    residentsMap.add(residentMap);
//                }
//            } catch (SQLException e) {
//                throw new RuntimeException("Error listing residents", e);
//            }
//
//            return residentsMap;
//        } catch (SQLException e) {
//            return null;
//        }
//    }
//
//    protected void insertSqlFile() {
//        String sqlPath = "src/test/java/com/sage/integration/data/inserts.sql";
//        try {
//            String sql = new String(Files.readAllBytes(Paths.get(sqlPath)));
//            connection.prepareStatement(sql).executeUpdate();
//        } catch (IOException | SQLException e) {
//            throw new RuntimeException("Error inserting SQL file", e);
//        }
//    }
//}
