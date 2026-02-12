// code by jph
package ch.alpine.sophus.hs.s;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class SnPhongMeanTest {
  @Test
  void testSnNormalized() {
    Distribution distribution = NormalDistribution.of(1, 0.2);
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, d).stream().map(Vector2Norm.NORMALIZE));
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, n));
        Tensor mean = SnPhongMean.INSTANCE.estimate(sequence, weights);
        Tolerance.CHOP.requireClose(mean, Vector2Norm.NORMALIZE.apply(mean));
      }
  }

  @Test
  void testMidpoint() {
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int d = 2; d < 4; ++d)
      for (int count = 0; count < 10; ++count) {
        Tensor x = Vector2Norm.NORMALIZE.apply(RandomVariate.of(distribution, d));
        Tensor y = Vector2Norm.NORMALIZE.apply(RandomVariate.of(distribution, d));
        Tensor m1 = SnManifold.INSTANCE.midpoint(x, y);
        SnManifold.INSTANCE.isPointQ().require(m1);
        Tensor m2 = SnManifold.INSTANCE.curve(x, y).apply(RationalScalar.HALF);
        SnManifold.INSTANCE.isPointQ().require(m2);
        Chop._08.requireClose(m1, m2);
        Tensor mp = SnPhongMean.INSTANCE.estimate(Tensors.of(x, y), Tensors.vector(0.5, 0.5));
        Chop._08.requireClose(m1, mp);
      }
  }

  @Test
  void testAffineFail() {
    Tensor x = UnitVector.of(3, 0);
    Tensor y = UnitVector.of(3, 1);
    SnPhongMean.INSTANCE.estimate(Tensors.of(x, y), Tensors.vector(0.5, 0.5));
    // assertThrows(Exception.class, () -> SnPhongMean.INSTANCE.mean(Tensors.of(x, y), Tensors.vector(0.5, 0.6)));
  }

  @Test
  void testAntipodalFail() {
    Tensor x = UnitVector.of(3, 0);
    Tensor y = x.negate();
    assertThrows(Exception.class, () -> SnPhongMean.INSTANCE.estimate(Tensors.of(x, y), Tensors.vector(0.5, 0.5)));
    // assertThrows(Exception.class, () -> SnPhongMean.INSTANCE.mean(Tensors.of(x, y), Tensors.vector(0.5, 0.6)));
  }
}
