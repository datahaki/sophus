// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class NormWeightingTest extends TestCase {
  public void testSimple() {
    Genesis inverseNorm = NormWeighting.of(Vector2Norm::of, InversePowerVariogram.of(1));
    Tensor weights = inverseNorm.origin(Tensors.vector(1, 3).map(Tensors::of));
    assertEquals(weights, Tensors.of(RationalScalar.of(3, 4), RationalScalar.of(1, 4)));
  }

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

  public void testFailNull() {
    AssertFail.of(() -> NormWeighting.of(null, InversePowerVariogram.of(1)));
    AssertFail.of(() -> NormWeighting.of(Vector2Norm::of, null));
  }
}
