package com.github.eokasta.sqlreader;

import com.github.eokasta.sqlreader.adapter.ReaderAdapterMap;
import com.github.eokasta.sqlreader.reader.ResultSetReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Program {

    private static final ReaderAdapterMap READER_ADAPTER_MAP = new ReaderAdapterMap();
    private static final ResultSetReader RESULT_SET_READER = new ResultSetReader(READER_ADAPTER_MAP);

    private static Connection connection;

    public static void main(String[] args) {
        openConnection();
        READER_ADAPTER_MAP
              .registerAdapter(String.class,
                    new TypeToken<ArrayList<Student>>() {
                    }.getRawType(),
                    new ListStudentAdapter()
              );

//        insert();
        read();
    }

    private static void read() {
        try (
              final PreparedStatement statement =
                    connection.prepareStatement("SELECT id, students FROM class_room WHERE id=1;");
              final ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet != null && resultSet.next()) {
                final ClassRoom classRoom = RESULT_SET_READER.parseSafe(resultSet, ClassRoom.class);
                System.out.println("Objeto da classe: " + classRoom);
                System.out.println("Classe " + classRoom.getId() + ":");
                for (Student student : classRoom.getStudents()) {
                    System.out.println();
                    System.out.println(" Estudante: " + student.getName());
                    System.out.println(" Idade: " + student.getAge());
                    System.out.println();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void insert() {
        try (final PreparedStatement statement =
                   connection.prepareStatement("INSERT INTO class_room (students) VALUES (?);")) {
            statement.setString(
                  1,
                  new Gson().toJson(Arrays.asList(new Student(UUID.randomUUID(), "gerso", 10)))
            );

            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void openConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_database", "root", null);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

}
