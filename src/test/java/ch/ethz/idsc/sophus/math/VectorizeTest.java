// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.opt.Pi;
import junit.framework.TestCase;

public class VectorizeTest extends TestCase {
  public void testSimple() {
    assertEquals(Vectorize.numel(HilbertMatrix.of(3), 0), 6);
    assertEquals(Vectorize.numel(HilbertMatrix.of(3), -1), 3);
    assertEquals(Vectorize.numel(HilbertMatrix.of(4), -1), 6);
  }

  public void testEmpty() {
    assertEquals(Vectorize.lt(Tensors.vector(), +0), Tensors.empty());
    assertEquals(Vectorize.lt(Tensors.vector(), -1), Tensors.empty());
  }

  public void testVectorFail() {
    try {
      Vectorize.lt(Tensors.vector(1, 2, 3), 0);
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      Vectorize.lt(Tensors.vector(1, 2, 3), -1);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testScalarFail() {
    try {
      Vectorize.lt(Pi.VALUE, 0);
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      Vectorize.lt(Pi.VALUE, -1);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
