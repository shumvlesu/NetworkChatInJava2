import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static HomeWork6.MainHW6.*;


public class MainHW6Test {

  @Test
  public void newArrayTest() {
    int[] input = {1, 2, 4, 4, 2, 3, 4, 1, 7};
    int[] output = {1, 7};
    Assertions.assertArrayEquals(output, newArray(input));
  }

  @Test
  public void newArrayTest2() {
    int[] input = {1, 2, 3, 4};
    int[] output = {};
    Assertions.assertArrayEquals(output, newArray(input));
  }

  @MethodSource("inputParam")
  @ParameterizedTest
  public void newArrayTest3(int[] input, int[] output) {
    Assertions.assertArrayEquals(output, newArray(input));
  }

  static Stream<Arguments> inputParam() {
    List<Arguments> result = new ArrayList<>();
    result.add(Arguments.arguments(new int[]{2, 3, 4}, new int[]{}));
    result.add(Arguments.arguments(new int[]{4, 2, 3}, new int[]{2, 3}));
    result.add(Arguments.arguments(new int[]{1, 4, 3}, new int[]{3}));
    //здесь метод выбросит исключение
    result.add(Arguments.arguments(new int[]{}, new int[]{}));
    return result.stream();
  }


  @MethodSource("intArrayProvider")
  @ParameterizedTest
  public void checkArrayTest(int[] input) {
    Assertions.assertTrue(checkArray(input));
  }

  static Stream<Arguments> intArrayProvider() {
    return Stream.of(
            Arguments.of((Object) new int[]{2, 3, 4}),
            Arguments.of((Object) new int[]{1, 2, 3}),
            Arguments.of((Object) new int[]{1, 2, 3, 4}),
            //эти два теста не пройдут
            Arguments.of((Object) new int[]{2, 3}),
            Arguments.of((Object) new int[]{})
    );
  }

}
