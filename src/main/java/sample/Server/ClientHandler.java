package sample.Server;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

  private Socket socket;
  private MyServer myServer;
  private DataInputStream dataInputStream;
  private DataOutputStream dataOutputStream;
  private String nick;

  public ClientHandler(MyServer myServer, Socket socket) {

    try {
      this.socket = socket;
      this.myServer = myServer;
      this.dataInputStream = new DataInputStream(socket.getInputStream());
      this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
      //создаем поток и стартуем его
      new Thread(() -> {
        try {
          //авторизация клиента
          authentication();
          //чтение ссобщений авторизованного клиента
          readMessages();
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          closeConnection();
        }
      }).start();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }


  //Метод авторизации клиента
  private void authentication() {
    while (true) {
      try {
        AuthMessage message = new Gson().fromJson(dataInputStream.readUTF(), AuthMessage.class);
        String nick = myServer.getAuthService().getNickByLoginAndPass(message.getLogin(), message.getPassword());
        if (nick != null && !myServer.isNickBusy(nick)) {
          message.setAuthenticated(true);
          message.setNick(nick);
          this.nick = nick;
          dataOutputStream.writeUTF(new Gson().toJson(message));
          Message broadcastMsg = new Message();
          broadcastMsg.setMessage(nick + " вошел в чат");
          myServer.broadcastMessage(broadcastMsg);
          myServer.subscribe(this);
          this.nick = nick;
          return;
        }

      } catch (IOException ignored) {
        // e.printStackTrace();
      }
    }
  }

  private void readMessages() throws IOException {
    while (true) {
      Message message = new Gson().fromJson(dataInputStream.readUTF(), Message.class);
      message.setNick(nick);
      System.out.println(message);
//      if ("/end".equals(message.getMessage())){
//        return;
//      }
      //если сообщение имеет "/" то либо это приватное соообщение либо команда выхода
      //проверяем что у нас нет - "/"
      if (!message.getMessage().startsWith("/")) {
        //рассылаем сообщение всем пользовотелям чата
        myServer.broadcastMessage(message);
        continue;
      }
      //дошли до сюда - значит сообщение приватное или команда выхода
      //разделяем строку сообщения на массив строк делиниатором выступает пробел
      String[] tokens = message.getMessage().split("\\s");
      //Первая чать сообщения до пробела
      switch (tokens[0]) {
        case "/end" -> {
          return;
        }
        case "/w" -> { //пример сообщения - /w никнеймПользователя телоСообщения
          //соответственно это 3 элемента массива tokens
          //и меньше 3х не должно быть, если это не так говорим об этом пользователю
          if (tokens.length < 3) {
            Message msg = new Message();
            msg.setMessage("Не хватает параметров, необходимо отправить команду следующего вида: /w <ник> <сообщение>");
            this.sendMessage(msg);
          }
          //Сам момент отправки приватного сообщения
          String nick = tokens[1];
          String msg = tokens[2];
          myServer.sendMsgToClient(this, nick, msg);
          //break;
        }
      }

    }
  }

  //метод послыает сообщение клиенту
  public void sendMessage(Message message) {
    try {
      dataOutputStream.writeUTF(new Gson().toJson(message));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getNick() {
    return nick;
  }

  private void closeConnection() {
    myServer.unsubscribe(this);
    Message message = new Message();
    message.setMessage(nick + " вышел из чата");
    myServer.broadcastMessage(message);
    try {
      dataOutputStream.close();
      dataInputStream.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
