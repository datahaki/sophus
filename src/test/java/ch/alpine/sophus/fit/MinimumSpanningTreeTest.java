// code by jph
package ch.alpine.sophus.fit;

import java.util.List;

import ch.alpine.sophus.math.IntUndirectedEdge;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.qty.Quantity;
import junit.framework.TestCase;

public class MinimumSpanningTreeTest extends TestCase {
  public void testSimple() {
    List<IntUndirectedEdge> list = MinimumSpanningTree.of(Tensors.fromString("{{0, 1}, {1, 0}}"));
    assertEquals(list.size(), 1);
  }

  public void testHilbert() {
    Tensor tensor = HilbertMatrix.of(10);
    List<IntUndirectedEdge> list = MinimumSpanningTree.of(tensor);
    assertEquals(list.size(), 9);
    Scalar scalar = list.stream().map(edge -> edge.Get(tensor)).reduce(Scalar::add).get();
    assertEquals(scalar, RationalScalar.of(1632341, 2450448));
  }

  public void testHilbertQuantity() {
    List<IntUndirectedEdge> list = MinimumSpanningTree.of(HilbertMatrix.of(10).map(s -> Quantity.of(s, "m")));
    assertEquals(list.size(), 9);
  }
}
