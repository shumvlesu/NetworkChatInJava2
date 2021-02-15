package sample.Client;

import org.apache.commons.io.input.ReversedLinesFileReader;
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileController {

  private final static String PATH_NAME = "src/main/resources/history.txt";
  private final static int HISTORY_STRING = 100;

  //1. Добавить в сетевой чат запись локальной истории в текстовый файл на клиенте.
  public static void saveToLogFile(String logString) {
    if (createFile()) {
      try {
        BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME, true));
        writer.write(logString);
        writer.newLine();
        writer.flush();//это буферизированный метод, надо сделать flush()
        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
  }

  private static boolean createFile() {
    try {
      File history = new File(PATH_NAME);
      if (!history.exists()) {
        history.createNewFile();
        System.out.println("Создан файл с истоией сообщений по пути:" + PATH_NAME);
      }
    } catch (IOException e) {
      System.out.println("Не получилось создать файл:" + PATH_NAME);
      e.printStackTrace();
      return false;
    }
    return true;
  }

  //2. После загрузки клиента показывать ему последние 100 строк чата.
  public static void loadLogFile(JTextArea mainChat) {
    //ReversedLinesFileReader - надеюсь это не читерство :)
    try (ReversedLinesFileReader reader = new ReversedLinesFileReader(new File(PATH_NAME), StandardCharsets.UTF_8)) {

      List result = new ArrayList<>();
      String line = "";
      while ((line = reader.readLine()) != null && result.size() < HISTORY_STRING) {
        result.add(line);
      }

      //Что-бы история не грузилась в обратном порядке, выгружаю в обратном порядке
      for (int i = result.size(); i > 0; i--) {
        mainChat.append(result.get(i - 1).toString() + "\n");
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

  }


}