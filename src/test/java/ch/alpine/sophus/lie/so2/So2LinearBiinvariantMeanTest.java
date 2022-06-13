// code by jph
package ch.alpine.sophus.lie.so2;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMeanTestHelper;
import ch.alpine.sophus.lie.ScalarBiinvariantMean;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.io.Primitives;
import ch.alpine.tensor.lie.Permutations;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

class So2LinearBiinvariantMeanTest {
  private static final Clip CLIP = Clips.absolute(Pi.VALUE);
  private static final ScalarBiinvariantMean[] SCALAR_BIINVARIANT_MEANS = { //
      So2PhongBiinvariantMean.INSTANCE, //
      So2LinearBiinvariantMean.INSTANCE };

  @Test
  void testPermutations() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(Pi.HALF));
    for (int length = 1; length < 6; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), length));
      for (ScalarBiinvariantMean so2BiinvariantMean : SCALAR_BIINVARIANT_MEANS) {
        Scalar solution = so2BiinvariantMean.mean(sequence, weights);
        for (int count = 0; count < 10; ++count) {
          Scalar shift = RandomVariate.of(distribution);
          for (Tensor perm : Permutations.of(Range.of(0, weights.length()))) {
            int[] index = Primitives.toIntArray(perm);
            Scalar result = so2BiinvariantMean.mean( //
                BiinvariantMeanTestHelper.order(sequence.map(shift::add), index), //
                BiinvariantMeanTestHelper.order(weights, index));
            CLIP.requireInside(result);
            Chop._12.requireAllZero(So2.MOD.apply(result.subtract(shift).subtract(solution)));
          }
        }
      }
    }
  }

  @Test
  void testSpecific() {
    Scalar scalar = So2LinearBiinvariantMean.INSTANCE.mean(Tensors.vector(3, 4), Tensors.vector(0.5, 0.5));
    Chop._12.requireClose(scalar, RealScalar.of(-2.7831853071795862));
  }

  @Test
  void testSame() {
    Scalar mean = So2LinearBiinvariantMean.INSTANCE.mean(Tensors.of(Pi.VALUE, Pi.VALUE.negate()), Tensors.vector(0.6, 0.4));
    Chop._12.requireClose(So2.MOD.apply(mean.subtract(Pi.VALUE)), RealScalar.ZERO);
  }

  @Test
  void testComparison() {
    Random random = new Random(123);
    Distribution distribution = UniformDistribution.of(Clips.absolute(Pi.HALF));
    Chop chop = Chop.below(0.7);
    for (int length = 1; length < 10; ++length) {
      final Tensor sequence = RandomVariate.of(distribution, random, length);
      final Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.of(1, 2), random, length));
      for (int count = 0; count < 10; ++count) {
        Scalar shift = RandomVariate.of(distribution, random);
        Scalar val1 = So2PhongBiinvariantMean.INSTANCE.mean(sequence.map(shift::add), weights);
        Tensor val2 = So2LinearBiinvariantMean.INSTANCE.mean(sequence.map(shift::add), weights);
        chop.requireAllZero(So2.MOD.apply(val1.subtract(val2)));
      }
    }
  }

  @Test
  void testFailAntipodal() {
    assertThrows(Exception.class, () -> So2LinearBiinvariantMean.INSTANCE.mean(Tensors.of(Pi.HALF, Pi.HALF.negate()), Tensors.vector(0.6, 0.4)));
  }
}
