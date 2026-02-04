// code by jph
package ch.alpine.sophus.bm;

import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.se2.Se2CoveringGroup;
import ch.alpine.sophus.math.StochasticMatrixQ;
import ch.alpine.tensor.Tensor;
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
    matrix = StochasticMatrixQ.INSTANCE.requireMember(Tensor.of(matrix.stream().map(Vector1Norm.NORMALIZE)));
    StochasticMatrixQ.INSTANCE.requireMember(Inverse.of(matrix));
    return matrix;
  }

  @Test
  void testRnSimple() {
    Distribution distribution = UniformDistribution.unit();
    RandomGenerator randomGenerator = new Random(3);
    for (int n = 1; n < 7; ++n) {
      Tensor sequence = RandomVariate.of(distribution, randomGenerator, n, 3);
      Tensor matrix = affine(n);
      Tensor invers = Inverse.of(matrix);
      Tensor mapped = Tensor.of(matrix.stream() //
          .map(weights -> LinearBiinvariantMean.INSTANCE.mean(sequence, weights)));
      Tensor result = Tensor.of(invers.stream() //
          .map(weights -> LinearBiinvariantMean.INSTANCE.mean(mapped, weights)));
      Chop._08.requireClose(sequence, result);
    }
  }

  @RepeatedTest(6)
  void testSe2CSimple(RepetitionInfo repetitionInfo) {
    int n = repetitionInfo.getCurrentRepetition();
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    Tensor matrix = affine(n);
    Tensor invers = Inverse.of(matrix);
    Tensor mapped = Tensor.of(matrix.stream() //
        .map(NormalizeTotal.FUNCTION) //
        .map(weights -> Se2CoveringGroup.INSTANCE.biinvariantMean().mean(sequence, weights)));
    Tensor result = Tensor.of(invers.stream() //
        .map(NormalizeTotal.FUNCTION) //
        .map(weights -> Se2CoveringGroup.INSTANCE.biinvariantMean().mean(mapped, weights)));
    Chop._08.requireClose(sequence.get(Tensor.ALL, 2), result.get(Tensor.ALL, 2));
  }
}
