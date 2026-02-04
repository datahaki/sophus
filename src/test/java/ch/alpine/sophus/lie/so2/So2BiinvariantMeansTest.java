package ch.alpine.sophus.lie.so2;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.math.Permute;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.lie.Permutations;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

class So2BiinvariantMeansTest {
  private static final Clip CLIP = Clips.absolute(Pi.VALUE);
  private static final BiinvariantMean[] SCALAR_BIINVARIANT_MEANS = { //
      So2BiinvariantMeans.GLOBAL, //
      So2BiinvariantMeans.LINEAR };

  @Test
  void testPermutations2() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(Pi.HALF));
    for (int length = 1; length < 6; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), length));
      for (BiinvariantMean so2BiinvariantMean : SCALAR_BIINVARIANT_MEANS) {
        Scalar solution = (Scalar) so2BiinvariantMean.mean(sequence, weights);
        for (int count = 0; count < 10; ++count) {
          Scalar shift = RandomVariate.of(distribution);
          for (Tensor perm : Permutations.of(Range.of(0, weights.length()))) {
            TensorUnaryOperator permute = Permute.of(perm);
            Scalar result = (Scalar) so2BiinvariantMean.mean( //
                permute.apply(sequence.map(shift::add)), //
                permute.apply(weights));
            CLIP.requireInside(result);
            Chop._12.requireAllZero(So2.MOD.apply(result.subtract(shift).subtract(solution)));
          }
        }
      }
    }
  }

  @Test
  void testSpecific() {
    Scalar scalar = (Scalar) So2BiinvariantMeans.LINEAR.mean(Tensors.vector(3, 4), Tensors.vector(0.5, 0.5));
    Chop._12.requireClose(scalar, RealScalar.of(-2.7831853071795862));
  }

  @Test
  void testSame() {
    Scalar mean = (Scalar) So2BiinvariantMeans.LINEAR.mean(Tensors.of(Pi.VALUE, Pi.VALUE.negate()), Tensors.vector(0.6, 0.4));
    Chop._12.requireClose(So2.MOD.apply(mean.subtract(Pi.VALUE)), RealScalar.ZERO);
  }

  @Test
  void testComparison() {
    RandomGenerator random = new Random(123);
    Distribution distribution = UniformDistribution.of(Clips.absolute(Pi.HALF));
    Chop chop = Chop.below(0.7);
    for (int length = 1; length < 10; ++length) {
      final Tensor sequence = RandomVariate.of(distribution, random, length);
      final Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.of(1, 2), random, length));
      for (int count = 0; count < 10; ++count) {
        Scalar shift = RandomVariate.of(distribution, random);
        Scalar val1 = (Scalar) So2BiinvariantMeans.GLOBAL.mean(sequence.map(shift::add), weights);
        Tensor val2 = So2BiinvariantMeans.LINEAR.mean(sequence.map(shift::add), weights);
        chop.requireAllZero(So2.MOD.apply(val1.subtract(val2)));
      }
    }
  }

  @Test
  void testFailAntipodal() {
    assertThrows(Exception.class, () -> So2BiinvariantMeans.LINEAR.mean(Tensors.of(Pi.HALF, Pi.HALF.negate()), Tensors.vector(0.6, 0.4)));
  }

  @Test
  void testSimple() {
    So2BiinvariantMeans.rangeQ(Tensors.vector(1, 2, 3));
    assertThrows(Exception.class, () -> So2BiinvariantMeans.rangeQ(Tensors.vector(1, 2, 7)));
  }

  @Test
  void testLength2Permutations() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    Distribution wd = UniformDistribution.of(-3, 3);
    for (int count = 0; count < 100; ++count) {
      Tensor sequence = RandomVariate.of(distribution, 2);
      Scalar w = RandomVariate.of(wd);
      Tensor weights = Tensors.of(RealScalar.ONE.subtract(w), w);
      Scalar mean1 = (Scalar) So2BiinvariantMeans.LINEAR.mean(sequence, weights);
      Scalar mean2 = (Scalar) So2BiinvariantMeans.FILTER.mean(Reverse.of(sequence), Reverse.of(weights));
      Chop._13.requireClose(mean1, mean2);
    }
  }

  @Test
  void testPermutations() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(Math.PI));
    for (int length = 1; length < 6; ++length) {
      // here, we hope that no antipodal points are generated
      Tensor sequence = RandomVariate.of(distribution, length);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), length));
      Scalar solution = (Scalar) So2BiinvariantMeans.GLOBAL.mean(sequence, weights);
      for (Tensor perm : Permutations.of(Range.of(0, weights.length()))) {
        TensorUnaryOperator permute = Permute.of(perm);
        Tensor result = So2BiinvariantMeans.GLOBAL.mean(permute.apply(sequence), permute.apply(weights));
        Chop._12.requireClose(result, solution);
      }
    }
  }

  @ParameterizedTest
  @EnumSource
  void testEmptyFail(So2BiinvariantMeans so2BiinvariantMeans) {
    assertThrows(Exception.class, () -> so2BiinvariantMeans.mean(Tensors.empty(), Tensors.empty()));
    assertThrows(Exception.class, () -> so2BiinvariantMeans.mean(HilbertMatrix.of(3), Tensors.vector(1, 1, 1).divide(RealScalar.of(3))));
  }
}
