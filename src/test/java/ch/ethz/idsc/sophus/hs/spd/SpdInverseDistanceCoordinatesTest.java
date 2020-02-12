// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdInverseDistanceCoordinatesTest extends TestCase {
  public void testSimple() {
    int d = 2;
    for (int len = 5; len < 10; ++len) {
      Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(d), len);
      Tensor point = TestHelper.generateSpd(d);
      Tensor weights = SpdInverseDistanceCoordinates.of(sequence, point);
      AffineQ.require(weights);
      Tensor spd = SpdMean.INSTANCE.mean(sequence, weights);
      Chop._08.requireClose(spd, point);
    }
  }

  public void test3x3() {
    int d = 3;
    int errors = 0;
    for (int len = 7; len < 16; ++len) {
      Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(d), len);
      Tensor point = TestHelper.generateSpd(d);
      Tensor weights = SpdInverseDistanceCoordinates.of(sequence, point);
      AffineQ.require(weights);
      try {
        Tensor spd = SpdMean.INSTANCE.mean(sequence, weights);
        Chop._08.requireClose(spd, point);
      } catch (Exception exception) {
        ++errors;
      }
    }
    assertTrue(errors < 8);
  }

  public void testLagrangeProperty() {
    int d = 2;
    for (int len = 5; len < 10; ++len) {
      Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(d), len);
      for (Tensor point : sequence) {
        Tensor weights = SpdInverseDistanceCoordinates.of(sequence, point);
        AffineQ.require(weights);
        Tensor spd = SpdMean.INSTANCE.mean(sequence, weights);
        Chop._08.requireClose(spd, point);
      }
    }
  }
}
