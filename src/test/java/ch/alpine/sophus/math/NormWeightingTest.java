// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.Genesis;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

public class NormWeightingTest {
  @Test
  public void testSimple() {
    Genesis inverseNorm = NormWeighting.of(Vector2Norm::of, InversePowerVariogram.of(1));
    Tensor weights = inverseNorm.origin(Tensors.vector(1, 3).map(Tensors::of));
    assertEquals(weights, Tensors.of(RationalScalar.of(3, 4), RationalScalar.of(1, 4)));
  }

  @Test
  public void testPoints() {
    Distribution distribution = UniformDistribution.unit();
    int j = 2;
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor tensor = RandomVariate.of(distribution, n, d);
        tensor.set(Scalar::zero, j, Tensor.ALL);
        Genesis inverseNorm = NormWeighting.of(Vector2Norm::of, InversePowerVariogram.of(1));
        for (int index = 0; index < tensor.length(); ++index) {
          Tensor q = inverseNorm.origin(tensor);
          Chop._10.requireClose(q, UnitVector.of(n, j));
        }
      }
  }

  @Test
  public void testFailNull() {
    assertThrows(Exception.class, () -> NormWeighting.of(null, InversePowerVariogram.of(1)));
    assertThrows(Exception.class, () -> NormWeighting.of(Vector2Norm::of, null));
  }
}
