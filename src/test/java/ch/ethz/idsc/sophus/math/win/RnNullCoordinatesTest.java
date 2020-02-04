// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class RnNullCoordinatesTest extends TestCase {
  public void testMean() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 3; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 2);
      Tensor x = RandomVariate.of(distribution, 2);
      TensorUnaryOperator rnNullCoordinates = RnNullCoordinates.of(Norm._2::ofVector, points);
      Tensor weights = rnNullCoordinates.apply(x);
      Tensor y = RnBiinvariantMean.INSTANCE.mean(points, weights);
      Chop._06.requireClose(x, y);
    }
  }
}
