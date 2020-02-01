// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Norm2Squared;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class InverseDistanceTest extends TestCase {
  public void testSimple() {
    InverseDistance inverseDistance = new InverseDistance(Norm2Squared::between);
    Tensor weights = inverseDistance.weights(Tensors.vector(1, 3).map(Tensors::of), RealScalar.of(2).map(Tensors::of));
    assertEquals(weights, Tensors.of(RationalScalar.HALF, RationalScalar.HALF));
  }

  public void testPoints() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 5; n < 10; ++n) {
      Tensor p1 = RandomVariate.of(distribution, n, 2);
      InverseDistance inverseDistance = new InverseDistance(Norm2Squared::between);
      for (int index = 0; index < p1.length(); ++index) {
        Tensor q = inverseDistance.weights(p1, p1.get(index));
        Chop._10.requireClose(q, UnitVector.of(n, index));
      }
    }
  }

  public void testFailNull() {
    try {
      new InverseDistance(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
