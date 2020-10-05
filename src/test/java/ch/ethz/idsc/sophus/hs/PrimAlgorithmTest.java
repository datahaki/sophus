// code by jph
package ch.ethz.idsc.sophus.hs;

import java.util.List;

import ch.ethz.idsc.sophus.hs.PrimAlgorithm.Edge;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import junit.framework.TestCase;

public class PrimAlgorithmTest extends TestCase {
  public void testSimple() {
    List<Edge> list = PrimAlgorithm.of(Tensors.fromString("{{0, 1}, {1, 0}}"));
    assertEquals(list.size(), 1);
  }

  public void testHilbert() {
    List<Edge> list = PrimAlgorithm.of(HilbertMatrix.of(10));
    System.out.println(list);
    assertEquals(list.size(), 9);
  }
}
