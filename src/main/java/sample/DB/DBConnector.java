package sample.DB;

import sample.Server.BaseAuthService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnector {
  public static final String TABLE_NAME = "users";
  public static final String PATH = "jdbc:sqlite:src/main/java/sample/DB/Accounts.s3db";

  private static Connection conn;
  private static Statement statement;

  public static void connectToDB() {
    try { //схема БД - testBD.s3db
      conn = DriverManager.getConnection(PATH);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public static void disconnectToDB (){
    try {
      statement.close();
      conn.close();
    }catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  //Создаем БД
  public static boolean createDB() {
    connectToDB();
    try {
      statement = conn.createStatement();
      //login должен быть уникальным
      statement.execute("CREATE TABLE IF NOT EXISTS '" + TABLE_NAME + "' (\n" +
              "\t 'nick' VARCHAR(500) NOT NULL COLLATE NOCASE,\n" +
              "\t 'password' VARCHAR(500) NOT NULL COLLATE NOCASE,\n" +
              "\t 'login' VARCHAR(500) PRIMARY KEY NOT NULL COLLATE NOCASE\n" +
              ");\n");

      //проверяю что таблица создана
      DatabaseMetaData dbm = conn.getMetaData();
      ResultSet databases = dbm.getTables(null, null, "%", null);

      while (databases.next()) {
        String databaseName = databases.getString(3);
        if (databaseName.equalsIgnoreCase(TABLE_NAME)) {
         // disconnectToDB ();
          return true;
        }
      }
      //disconnectToDB ();
      return false;
    } catch (SQLException throwables) {
      //disconnectToDB ();
      throwables.printStackTrace();
      return false;
    }finally {
      disconnectToDB();
    }

  }

  public static void createNewUser(String login, String password, String nick) {
    connectToDB();
    try {
      //PreparedStatement preparedStatement = conn.prepareStatement("select * from students where Score > ?");

      //Вопрос преподователю: Какой способ подставления TABLE_NAME более предпочтителен или разницы нет? Или есть более лучший способ? С "?" у меня не получилось.
      //PreparedStatement preparedStatement = conn.prepareStatement("insert into "+TABLE_NAME+" (login,password,nick) values(?,?,?)");
      String strQuery = "insert into $tableName (login,password,nick) values(?,?,?)";
      String query =strQuery.replace("$tableName",TABLE_NAME);
      PreparedStatement preparedStatement = conn.prepareStatement(query);
      preparedStatement.setString(1, login);
      preparedStatement.setString(2, password);
      preparedStatement.setString(3, nick);
      preparedStatement.execute();

    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }finally {
      disconnectToDB();
    }

  }

  public static <S>String[] getUserInDB(String login){
    connectToDB();
    try {
      PreparedStatement preparedStatement = conn.prepareStatement("select * from "+TABLE_NAME+" where login = ?");
      preparedStatement.setString(1,login);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()){
        return new String[] {resultSet.getString("login"), resultSet.getString("password"), resultSet.getString("nick")};
      }else {
        return new String[0];
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }finally {
      disconnectToDB();
    }
    return new String[0];
  }


  public static String changeNick(String oldNick,String newNick) {
    connectToDB();
    try {

      String sql = "update "+TABLE_NAME+" set nick=? where nick=?";
      PreparedStatement preparedStatement = conn.prepareStatement(sql);
      preparedStatement.setString(1, newNick);
      preparedStatement.setString(2, oldNick);
      int rowsAffected = preparedStatement.executeUpdate();
      if (rowsAffected > 0){
        return "Данные изменены в БД";
      }else {
        return "Данные не были найдены в БД";
      }

    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }finally {
      disconnectToDB();
    }
    return "";
  }


}
