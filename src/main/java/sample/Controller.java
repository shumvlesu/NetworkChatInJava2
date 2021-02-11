package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.DB.DBConnector;
import sample.Server.MyServer;

public class Controller {

  @FXML
  private Label information;

  @FXML
  private TextField oldNick;

  @FXML
  private TextField newNick;

  @FXML
  public void initialize() {
    new Thread(() -> {
      new MyServer();
    }).start();
  }

  public void onChangeNick(ActionEvent actionEvent) {

    String oldNickS = oldNick.getText();
    String newNickS = newNick.getText();
    if (!oldNickS.isBlank() && !newNickS.isBlank()) {
      information.setText(DBConnector.changeNick(oldNickS,newNickS));
    }

  }
}
