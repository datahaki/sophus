// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class RnBiinvariantCoordinateTest extends TestCase {
  public void testColinear() {
    int d = 2;
    int n = 5;
    Tensor sequence = RandomVariate.of(NormalDistribution.standard(), n, d);
    sequence.append(sequence.get(n - 1).multiply(RealScalar.of(5)));
    Tensor weights = RnBiinvariantCoordinate.INSTANCE.weights(sequence, Array.zeros(d));
    assertEquals(sequence.length(), n + 1);
    AffineQ.require(weights);
  }
}
