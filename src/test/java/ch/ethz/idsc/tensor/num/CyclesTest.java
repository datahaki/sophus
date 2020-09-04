// code by jph
package ch.ethz.idsc.tensor.num;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.Serialization;
import junit.framework.TestCase;

public class CyclesTest extends TestCase {
  public void testSingleton() throws ClassNotFoundException, IOException {
    Cycles cycles = Serialization.copy(Cycles.of(Tensors.fromString("{{5, 9}, {7}, {}}")));
    assertEquals(cycles.toTensor(), Tensors.of(Tensors.vector(5, 9)));
  }

  public void testSimple() {
    Cycles cycles = Cycles.of(Tensors.fromString("{{5, 9}, {7, 14, 13}, {18, 4, 10, 19, 6}, {20, 1}, {}}"));
    Tensor tensor = cycles.toTensor();
    assertEquals(tensor, Tensors.fromString("{{1, 20}, {4, 10, 19, 6, 18}, {5, 9}, {7, 14, 13}}"));
  }

  public void testInverse() {
    Cycles cycles = Cycles.of(Tensors.fromString("{{1, 20}, {4, 10, 19, 6, 18}, {5, 9}, {7, 14, 13}}"));
    assertEquals(InversePermutation.of(cycles).toTensor(), //
        Tensors.fromString("{{1, 20}, {4, 18, 6, 19, 10}, {5, 9}, {7, 13, 14}}"));
  }

  private static String _combo(String a, String b) {
    return PermutationProduct.of(Cycles.of(Tensors.fromString(a)), Cycles.of(Tensors.fromString(b))).toTensor().toString();
  }

  public void testCombine() {
    assertEquals(_combo("{{1, 2, 3}}", "{{3, 4}}"), "{{1, 2, 4, 3}}");
    assertEquals(_combo("{{1, 2}, {4, 5}}", "{{3, 4}}"), "{{1, 2}, {3, 4, 5}}");
    assertEquals(_combo("{{1, 2}, {4, 5}}", "{{3, 4, 5}}"), "{{1, 2}, {3, 4}}");
    assertEquals(_combo("{{1, 2, 3}, {4, 5}}", "{{3, 4, 5}}"), "{{1, 2, 4, 3}}");
    assertEquals(_combo("{{2, 3}, {4, 5}}", "{{3, 4, 1}}"), "{{1, 3, 2, 4, 5}}");
  }

  public void testCleanMap() {
    Map<Integer, Integer> map = new HashMap<>();
    map.put(1, 3);
    map.put(3, 1);
    Cycles cycles = PermutationProduct.of(Cycles.of(map), Cycles.of(map));
    assertTrue(cycles.map().isEmpty());
    assertTrue(cycles.equals(Cycles.of(Tensors.fromString("{{}}"))));
  }

  public void testEmpty() {
    assertEquals(Cycles.of(Tensors.empty()).toTensor(), Tensors.empty());
  }

  public void testMap2Tensor() {
    Map<Integer, Integer> map = new HashMap<>();
    map.put(1, 3);
    map.put(3, 1);
    map.put(4, 4);
    map.put(5, 2);
    map.put(2, 7);
    map.put(7, 5);
    assertEquals( //
        Cycles.of(map).toTensor(), //
        Tensors.fromString("{{1, 3}, {2, 7, 5}}"));
  }

  public void testScalarFail() {
    try {
      Cycles.of(Tensors.fromString("{3}"));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testDuplicateFail() {
    try {
      Cycles.of(Tensors.fromString("{{5, 5}, {3}, {2, 2, 2}}"));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testNegativeFail() {
    try {
      Cycles.of(Tensors.fromString("{{-3}}"));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
