// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import junit.framework.TestCase;

public class VectorizeTest extends TestCase {
  public void testSimple() {
    assertEquals(Vectorize.numel(HilbertMatrix.of(3), 0), 6);
    assertEquals(Vectorize.numel(HilbertMatrix.of(3), -1), 3);
    assertEquals(Vectorize.numel(HilbertMatrix.of(4), -1), 6);
  }
}
