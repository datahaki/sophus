// code by jph
package ch.alpine.sophus.bm;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.lie.se2c.Se2CoveringBiinvariantMean;
import ch.alpine.sophus.math.StochasticMatrixQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.nrm.Vector1Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class BiinvariantMeanTest {
  private static final Distribution DISTRIBUTION = UniformDistribution.unit();

  /** @param n
   * @return */
  private static Tensor affine(int n) {
    Tensor matrix = RandomVariate.of(DISTRIBUTION, n, n);
    matrix = StochasticMatrixQ.requireRows(Tensor.of(matrix.stream().map(Vector1Norm.NORMALIZE)), Chop._08);
    StochasticMatrixQ.requireRows(Inverse.of(matrix), Chop._10);
    return matrix;
  }

  @Test
  void testRnSimple() {
    Distribution distribution = UniformDistribution.unit();
    Random random = new Random(3);
    for (int n = 1; n < 7; ++n) {
      Tensor sequence = RandomVariate.of(distribution, random, n, 3);
      Tensor matrix = affine(n);
      Tensor invers = Inverse.of(matrix);
      Tensor mapped = Tensor.of(matrix.stream() //
          .map(weights -> RnBiinvariantMean.INSTANCE.mean(sequence, weights)));
      Tensor result = Tensor.of(invers.stream() //
          .map(weights -> RnBiinvariantMean.INSTANCE.mean(mapped, weights)));
      Chop._08.requireClose(sequence, result);
    }
  }

  @Test
  void testSe2CSimple() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 1; n < 7; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 3);
      Tensor matrix = affine(n);
      Tensor invers = Inverse.of(matrix);
      Tensor mapped = Tensor.of(matrix.stream() //
          .map(NormalizeTotal.FUNCTION) //
          .map(weights -> Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights)));
      Tensor result = Tensor.of(invers.stream() //
          .map(NormalizeTotal.FUNCTION) //
          .map(weights -> Se2CoveringBiinvariantMean.INSTANCE.mean(mapped, weights)));
      Tolerance.CHOP.requireClose(sequence.get(Tensor.ALL, 2), result.get(Tensor.ALL, 2));
    }
  }
}
