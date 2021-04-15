package com.github.eokasta.sqlreader;

import com.github.eokasta.sqlreader.adapter.ReaderAdapterMap;
import com.github.eokasta.sqlreader.provider.MacacoAdapter;
import com.github.eokasta.sqlreader.reader.ResultSetReader;

import java.sql.*;

public class MySQLReaderPlugin {

    private static final ReaderAdapterMap READER_ADAPTER_MAP = new ReaderAdapterMap();
    private static final ResultSetReader RESULT_SET_ADAPTER = new ResultSetReader(READER_ADAPTER_MAP);

    private static Connection connection;

    public static void main(String[] args) {
        System.out.println("\n\n\n\n\n\n\n\n\n\n");
        createConnection();
        READER_ADAPTER_MAP.registerAdapter(String.class, Macaco.class, new MacacoAdapter());

        try (
              final PreparedStatement statement =
                    connection.prepareStatement("SELECT name, age, macaco FROM test WHERE name = 'gerson'");
              final ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet != null && resultSet.next())
                System.out.println(RESULT_SET_ADAPTER.getFromSafe(resultSet, AClass.class));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager
                  .getConnection("jdbc:mysql://localhost:3306/test_database", "root", null);

            System.out.println(connection != null ? "Conectado no banco!" : "Saporra n conecto!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

}
