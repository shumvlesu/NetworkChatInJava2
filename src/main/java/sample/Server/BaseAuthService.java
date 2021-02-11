package sample.Server;

import sample.DB.DBConnector;

import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {

  //private List<Entry> entries;

  public BaseAuthService() {
//    //закоментить
//    entries = new ArrayList<>();
//    entries.add(new Entry("ivan", "123", "Neivanov"));
//    entries.add(new Entry("sharik", "111", "Gav"));
//    entries.add(new Entry("cat", "321", "murzik"));

    if (DBConnector.createDB()){
        DBConnector.createNewUser("ivan", "123", "Neivanov");
        DBConnector.createNewUser("sharik", "111", "Gav");
        DBConnector.createNewUser("cat", "321", "murzik");
      };

  }

  //закоментить
//  private class Entry{
//
//    private String login;
//    private String password;
//    private String nick;
//
//    public Entry(String login, String password, String nick) {
//      this.login = login;
//      this.password = password;
//      this.nick = nick;
//    }
//
//  }

  @Override
  public void start() {
    System.out.println("Сервис авторизации запущен");
  }

  @Override
  public void stop() {
    System.out.println("Сервис авторизации остановлен");
  }

  @Override
  public String getNickByLoginAndPass(String login, String password) {

    String[] entryString = DBConnector.getUserInDB(login);

    if (entryString.length>0) {
      if (login.equals(entryString[0]) && password.equals(entryString[1])){
        return entryString[2];
      }
    }

//    for (Entry entry : entries) {
//      if (login.equals(entry.login) && password.equals(entry.password)){
//        return entry.nick;
//      }
//    }
    return null;
  }
}
