// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.red.Mean;
import ch.alpine.tensor.sca.Sign;

class ArrowheadTest {
  @Test
  void testOriented() {
    Tensor polygon = Arrowhead.of(1);
    Scalar scalar = PolygonArea.of(polygon);
    assertEquals(scalar, RationalScalar.HALF);
    Sign.requirePositive(scalar);
    Tensor centroid = PolygonCentroid.of(polygon);
    ExactTensorQ.require(centroid);
    assertEquals(centroid, Array.zeros(2));
  }

  @Test
  void testExact() {
    ExactTensorQ.require(Arrowhead.of(RealScalar.ONE));
  }

  @Test
  void testLength() {
    assertEquals(Arrowhead.of(6).length(), 3);
  }

  @Test
  void testMean() {
    Tensor tensor = Mean.of(Arrowhead.of(2));
    assertEquals(tensor, tensor.map(Scalar::zero));
  }
}
