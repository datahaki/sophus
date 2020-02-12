// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnInverseDistanceCoordinatesTest extends TestCase {
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);

  public void testSimple() {
    Distribution distribution = NormalDistribution.of(0, 0.4);
    for (int count = 0; count < 10; ++count) {
      Tensor mean = UnitVector.of(3, 0);
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, 4, 3).stream().map(mean::add).map(NORMALIZE));
      Tensor weights = SnInverseDistanceCoordinates.of(sequence, mean);
      VectorQ.requireLength(weights, 4);
      AffineQ.require(weights);
      Tensor evaluate = SnMean.defect(sequence, weights, mean);
      Chop._12.requireAllZero(evaluate);
      Tensor point = SnMean.INSTANCE.mean(sequence, weights);
      Chop._12.requireClose(mean, point);
    }
  }
}
