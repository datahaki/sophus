// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.opt.Pi;
import junit.framework.TestCase;

public class NormalizeTotalTest extends TestCase {
  public void testSimple() {
    Tensor tensor = NormalizeTotal.FUNCTION.apply(Tensors.vector(2, -3, 4, 5));
    assertEquals(tensor, Tensors.fromString("{1/4, -3/8, 1/2, 5/8}"));
  }

  public void testEmpty() {
    try {
      NormalizeTotal.FUNCTION.apply(Tensors.empty());
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testZeroFail() {
    try {
      NormalizeTotal.FUNCTION.apply(Tensors.vector(2, -2, 1, -1));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testZeroNumericFail() {
    try {
      NormalizeTotal.FUNCTION.apply(Tensors.vectorDouble(2, -2, 1, -1));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFailScalar() {
    try {
      NormalizeTotal.FUNCTION.apply(Pi.TWO);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFailMatrix() {
    try {
      NormalizeTotal.FUNCTION.apply(HilbertMatrix.of(3));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
