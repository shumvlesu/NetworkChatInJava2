package sample.Server;

public interface AuthService {
  void start();
  void stop();
  String getNickByLoginAndPass(String login, String password);
}