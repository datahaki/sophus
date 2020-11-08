// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class AppendOneTest extends TestCase {
  public void testSimple() {
    Tensor vector = Tensors.vector(2, 3);
    Tensor result = AppendOne.FUNCTION.apply(vector);
    assertEquals(vector, Tensors.vector(2, 3));
    assertEquals(result, Tensors.vector(2, 3, 1));
  }
}
