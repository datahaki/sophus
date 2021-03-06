// code by jph
package ch.ethz.idsc.sophus.bm;

import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringBiinvariantMean;
import ch.ethz.idsc.sophus.math.StochasticMatrixQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.nrm.Vector1Norm;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class BiinvariantMeanTest extends TestCase {
  private static final Distribution DISTRIBUTION = UniformDistribution.unit();

  /** @param n
   * @return */
  private static Tensor affine(int n) {
    Tensor matrix = RandomVariate.of(DISTRIBUTION, n, n);
    matrix = StochasticMatrixQ.requireRows(Tensor.of(matrix.stream().map(Vector1Norm.NORMALIZE)), Chop._08);
    StochasticMatrixQ.requireRows(Inverse.of(matrix), Chop._10);
    return matrix;
  }

  public void testRnSimple() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 1; n < 7; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 3);
      Tensor matrix = affine(n);
      Tensor invers = Inverse.of(matrix);
      Tensor mapped = Tensor.of(matrix.stream() //
          .map(weights -> RnBiinvariantMean.INSTANCE.mean(sequence, weights)));
      Tensor result = Tensor.of(invers.stream() //
          .map(weights -> RnBiinvariantMean.INSTANCE.mean(mapped, weights)));
      Chop._10.requireClose(sequence, result);
    }
  }

  public void testSe2CSimple() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 1; n < 7; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 3);
      Tensor matrix = affine(n);
      Tensor invers = Inverse.of(matrix);
      Tensor mapped = Tensor.of(matrix.stream() //
          .map(weights -> Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights)));
      Tensor result = Tensor.of(invers.stream() //
          .map(weights -> Se2CoveringBiinvariantMean.INSTANCE.mean(mapped, weights)));
      Chop._10.requireClose(sequence.get(Tensor.ALL, 2), result.get(Tensor.ALL, 2));
    }
  }
}
