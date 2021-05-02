// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Range;
import junit.framework.TestCase;

public class DecibelTest extends TestCase {
  public void testSimple() {
    Scalar scalar = Decibel.FUNCTION.apply(RealScalar.of(100));
    assertEquals(scalar, RealScalar.of(40));
  }

  public void testVector() {
    Tensor tensor = Decibel.of(Range.of(1, 100));
    assertEquals(tensor.length(), 99);
  }

  public void testNegativeInfinity() {
    Scalar scalar = Decibel.FUNCTION.apply(RealScalar.ZERO);
    assertEquals(scalar, DoubleScalar.NEGATIVE_INFINITY);
  }
}
