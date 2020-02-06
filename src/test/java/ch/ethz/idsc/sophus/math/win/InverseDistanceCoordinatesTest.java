// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class InverseDistanceCoordinatesTest extends TestCase {
  public void testLinearReproduction() {
    Distribution distribution = UniformDistribution.unit();
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, d);
        Tensor x = RandomVariate.of(distribution, d);
        TensorUnaryOperator idfCoordinates = InverseDistanceCoordinates.of(Norm._2::ofVector, points);
        Tensor weights = idfCoordinates.apply(x);
        Tensor y = RnBiinvariantMean.INSTANCE.mean(points, weights);
        Chop._06.requireClose(x, y);
      }
  }

  public void testLagrangeProperty() {
    Distribution distribution = UniformDistribution.unit();
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, d);
        TensorUnaryOperator idfCoordinates = InverseDistanceCoordinates.of(Norm._2::ofVector, points);
        Chop._06.requireClose(Tensor.of(points.stream().map(idfCoordinates)), IdentityMatrix.of(n));
      }
  }
}
