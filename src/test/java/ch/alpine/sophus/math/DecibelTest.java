// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Range;

class DecibelTest {
  @Test
  void testSimple() {
    Scalar scalar = Decibel.FUNCTION.apply(RealScalar.of(100));
    assertEquals(scalar, RealScalar.of(40));
  }

  @Test
  void testVector() {
    Tensor tensor = Range.of(1, 100).maps(Decibel.FUNCTION);
    assertEquals(tensor.length(), 99);
  }

  @Test
  void testNegativeInfinity() {
    Scalar scalar = Decibel.FUNCTION.apply(RealScalar.ZERO);
    assertEquals(scalar, DoubleScalar.NEGATIVE_INFINITY);
  }
}
