// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.sophus.lie.rn.RnVectorNorm;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class InverseDistanceCoordinatesTest extends TestCase {
  public void testLinearReproduction() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.unit();
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, d);
        Tensor x = RandomVariate.of(distribution, d);
        TensorUnaryOperator tensorUnaryOperator = //
            Serialization.copy(InverseDistanceCoordinates.of(RnVectorNorm.INSTANCE, points));
        Tensor weights = tensorUnaryOperator.apply(x);
        Tensor y = RnBiinvariantMean.INSTANCE.mean(points, weights);
        Chop._10.requireClose(x, y);
      }
  }

  public void testLagrangeProperty() {
    Distribution distribution = UniformDistribution.unit();
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, d);
        TensorUnaryOperator tensorUnaryOperator = InverseDistanceCoordinates.of(RnVectorNorm.INSTANCE, points);
        Chop._10.requireClose(Tensor.of(points.stream().map(tensorUnaryOperator)), IdentityMatrix.of(n));
      }
  }

  public void testQuantity() {
    Distribution distribution = UniformDistribution.of(Quantity.of(-1, "m"), Quantity.of(+1, "m"));
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, d);
        Tensor x = RandomVariate.of(distribution, d);
        TensorUnaryOperator tensorUnaryOperator = InverseDistanceCoordinates.of(RnVectorNorm.INSTANCE, points);
        Tensor weights = tensorUnaryOperator.apply(x);
        Tensor y = RnBiinvariantMean.INSTANCE.mean(points, weights);
        Chop._10.requireClose(x, y);
      }
  }
}
