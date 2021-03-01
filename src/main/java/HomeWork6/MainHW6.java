package HomeWork6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainHW6 {
//1. Написать метод, которому в качестве аргумента передается не пустой одномерный целочисленный массив.
// Метод должен вернуть новый массив, который получен путем вытаскивания из исходного массива элементов,
// идущих после последней четверки. Входной массив должен содержать хотя бы одну четверку,
// иначе в методе необходимо выбросить RuntimeException.
// Написать набор тестов для этого метода (по 3-4 варианта входных данных). Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].

  public static int[] newArray(int[] input) {
    final int separator = 4;
    boolean dotSeparator = false;

    if (input.length == 0) throw new RuntimeException("В массиве нет элементов");

    List<Integer> outArray = new ArrayList<>();

    //читаем массив с конца
    for (int i = input.length - 1; i >= 0; i--) {
      if (input[i] == separator) {
        dotSeparator = true;
        break;
      }
      outArray.add(input[i]);
    }

    if (!dotSeparator) throw new RuntimeException("В массиве нет " + separator);

    //реверсируем массив
    Collections.reverse(outArray);

    return outArray.stream().mapToInt(Integer::intValue).toArray();
  }


  //2. Написать метод, который проверяет состав массива из чисел 1 и 4.
  // Если в нем нет хоть одной четверки или единицы, то метод вернет false;
  // Написать набор тестов для этого метода (по 3-4 варианта входных данных).
  public static boolean checkArray(int[] input) {

    for (int element : input) {
      if (element == 1 || element == 4) {
        return true;
      }
    }
    return false;
  }

}
