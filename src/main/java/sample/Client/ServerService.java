package sample.Client;

import sample.Server.Message;

import java.io.IOException;

//Интерфейс отвечает за установку; открытие/закрытие; соединения, авторизацию, посылку сообщений
public interface ServerService {

  boolean isConnected();
  void openConnection();
  void closeConnection();
  String authorization(String login, String password) throws IOException;
  void sendMessage(String message);

  Message readMessages();

}
