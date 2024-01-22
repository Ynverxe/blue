package com.github.ynverxe.storage.sql;

import com.github.ynverxe.blue.storage.crud.repository.types.TextCrudRepository;
import com.github.ynverxe.storage.sql.repository.SQLTextRepository;
import com.github.ynverxe.storage.sql.query.defaults.MySQLJsonQueryFactory;
import com.github.ynverxe.storage.sql.source.ConnectionSource;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(DatabaseConnectionChecker.class)
public class SQLRepositoryTest {

  private static final Gson GSON = new Gson();
  private static final TextCrudRepository REPOSITORY;

  static {
    try {
      Connection connection = MySQLConnectionProvider.CONNECTION;

      if (connection != null) {
        dropTestTable();

        REPOSITORY = new SQLTextRepository(
          new MySQLJsonQueryFactory("JSON_TABLE_TEST", "id", "jsonValue"), new ConnectionSource(MySQLConnectionProvider.CONNECTION)
        );
      } else {
        REPOSITORY = null;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  @Order(0)
  public void testDataPersistence() {
    String json = "{\"name\":\"ynverxe\"}";
    REPOSITORY.save("jsonTest", json);
    String savedValue = REPOSITORY.read("jsonTest");
    assertNotNull(savedValue);
    assertEquals(GSON.fromJson(json, JsonObject.class), GSON.fromJson(savedValue, JsonObject.class));
  }

  @Test
  @Order(1)
  public void testDeletion() {
    assertTrue(REPOSITORY.delete("jsonTest"));
  }

  @Test
  @Order(2)
  public void testFindAll() {
    String firstJson = "{}";
    String secondJson = "{\"name\":\"ynverxe\"}";

    REPOSITORY.save("firstJson", firstJson);
    REPOSITORY.save("secondJson", secondJson);

    Map<String, String> model = new LinkedHashMap<>();
    model.put("firstJson", firstJson);
    model.put("secondJson", secondJson);

    assertEquals(model, REPOSITORY.readAll());
  }

  @AfterAll
  public static void clear() throws Exception {
    dropTestTable();
  }

  private static void dropTestTable() throws Exception {
    Connection connection = MySQLConnectionProvider.CONNECTION;
    Statement statement = connection.createStatement();

    statement.execute("DROP TABLE IF EXISTS JSON_TABLE_TEST");
    statement.close();
  }
}