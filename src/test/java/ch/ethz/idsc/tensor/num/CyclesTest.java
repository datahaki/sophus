// code by jph
package ch.ethz.idsc.tensor.num;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.opt.Pi;
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
    Cycles ca = Cycles.of(Tensors.fromString(a));
    assertTrue(1 < ca.map().size());
    assertEquals(ca.product(ca.inverse()), Cycles.identity());
    assertEquals(ca.inverse().product(ca), Cycles.identity());
    Cycles cb = Cycles.of(Tensors.fromString(b));
    assertTrue(1 < cb.map().size());
    assertEquals(cb.product(cb.inverse()), Cycles.identity());
    assertEquals(cb.inverse().product(cb), Cycles.identity());
    return PermutationProduct.of(ca, cb).toString();
  }

  public void testCombine() {
    assertEquals(_combo("{{1, 2, 3}}", "{{3, 4}}"), "{{1, 2, 4, 3}}");
    assertEquals(_combo("{{1, 2}, {4, 5}}", "{{3, 4}}"), "{{1, 2}, {3, 4, 5}}");
    assertEquals(_combo("{{1, 2}, {4, 5}}", "{{3, 4, 5}}"), "{{1, 2}, {3, 4}}");
    assertEquals(_combo("{{1, 2, 3}, {4, 5}}", "{{3, 4, 5}}"), "{{1, 2, 4, 3}}");
    assertEquals(_combo("{{2, 3}, {4, 5}}", "{{3, 4, 1}}"), "{{1, 3, 2, 4, 5}}");
    assertEquals(_combo("{{2, 3}}", "{{1, 2, 3}}"), "{{1, 2}}");
    assertEquals(_combo("{{2, 7, 3}, {4, 5}}", "{{1, 2, 3}, {7, 4, 5, 6}}"), "{{1, 2, 4, 6, 7}}");
    assertEquals(_combo("{{2, 7, 3}, {1, 6}, {4, 5}}", "{{1, 2, 3}, {7, 4, 5, 6}}"), "{{1, 7}, {2, 4, 6}}");
  }

  public void testCleanMap() throws ClassNotFoundException, IOException {
    Map<Integer, Integer> map = new HashMap<>();
    map.put(1, 3);
    map.put(3, 1);
    Cycles cycles = PermutationProduct.of(Cycles.from(map), Cycles.from(map));
    assertTrue(cycles.map().isEmpty());
    assertEquals(cycles, Cycles.of(Tensors.fromString("{{}}")));
    assertEquals(cycles, Cycles.identity());
    assertEquals(cycles, Serialization.copy(Cycles.identity()));
    assertEquals(cycles.hashCode(), Cycles.identity().hashCode());
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
        Cycles.from(map).toTensor(), //
        Tensors.fromString("{{1, 3}, {2, 7, 5}}"));
  }

  public void testNonEquals() throws ClassNotFoundException, IOException {
    Cycles cycles = Serialization.copy(Cycles.of(Tensors.fromString("{{5, 9}, {7}, {}}")));
    assertFalse(cycles.equals(Pi.VALUE));
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
    try {
      Cycles.of(Tensors.fromString("{{3, -0.1}}"));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
