// code by jph
package ch.alpine.sophus.lie.se2;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.LinearBiinvariantMean;
import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.math.Permute;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.lie.Permutations;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.QuantityMagnitude;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class Se2CoveringBiinvariantMeanTest {
  @Test
  void testPermutations() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int length = 1; length < 6; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor weights = RandomVariate.of(distribution, length);
      weights = weights.divide(Total.ofVector(weights));
      Tensor solution = Se2CoveringGroup.INSTANCE.biinvariantMean().mean(sequence, weights);
      for (Tensor perm : Permutations.of(Range.of(0, weights.length()))) {
        TensorUnaryOperator permute = Permute.of(perm);
        Tensor result = Se2CoveringGroup.INSTANCE.biinvariantMean().mean(permute.apply(sequence), permute.apply(weights));
        Chop._03.requireClose(result, solution);
      }
    }
  }

  @Test
  void testEquation() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int length = 1; length < 6; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor weights = RandomVariate.of(distribution, length);
      weights = weights.divide(Total.ofVector(weights));
      Tensor mean = Se2CoveringGroup.INSTANCE.biinvariantMean().mean(sequence, weights);
      Tensor defect = new MeanDefect(sequence, weights, Se2CoveringGroup.INSTANCE.exponential(mean)).tangent();
      Chop._04.requireClose(defect, defect.map(Scalar::zero)); // 1e-6 does not always work
    }
  }

  @Test
  void testEquationQuantity() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int length = 1; length < 6; ++length) {
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, length, 3).stream().map(row -> Tensors.of( //
          Quantity.of(row.Get(0), "m"), //
          Quantity.of(row.Get(1), "m"), //
          Quantity.of(row.Get(2), ""))));
      Tensor weights = RandomVariate.of(distribution, length);
      weights = weights.divide(Total.ofVector(weights));
      Tensor mean = Se2CoveringGroup.INSTANCE.biinvariantMean().mean(sequence, weights);
      QuantityMagnitude.SI().in("m").apply(mean.Get(0));
      QuantityMagnitude.SI().in("m").apply(mean.Get(1));
      Tensor defect = new MeanDefect(sequence, weights, Se2CoveringGroup.INSTANCE.exponential(mean)).tangent();
      Chop._06.requireClose(defect, defect.map(Scalar::zero));
    }
  }

  @Test
  void testIdentityXY() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(8));
    for (int length = 1; length < 6; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      sequence.set(_ -> RealScalar.ZERO, Tensor.ALL, 2);
      Tensor weights = RandomVariate.of(distribution, length);
      weights = weights.divide(Total.ofVector(weights));
      Tensor solution = Se2CoveringGroup.INSTANCE.biinvariantMean().mean(sequence, weights);
      for (Tensor perm : Permutations.of(Range.of(0, weights.length()))) {
        TensorUnaryOperator permute = Permute.of(perm);
        Tensor result = Se2CoveringGroup.INSTANCE.biinvariantMean().mean(permute.apply(sequence), permute.apply(weights));
        Chop._08.requireClose(result, solution);
        Tensor rnmean = LinearBiinvariantMean.INSTANCE.mean(permute.apply(sequence), permute.apply(weights));
        Chop._02.requireClose(result, rnmean);
      }
    }
  }

  @Test
  void testRandom() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 4; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 3);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, n));
      Tensor xya = Se2CoveringGroup.INSTANCE.biinvariantMean().mean(points, weights);
      Tensor all = Tensor.of(points.stream().map(Se2CoveringGroup.INSTANCE::invert));
      Tensor one = Se2CoveringGroup.INSTANCE.biinvariantMean().mean(all, weights);
      Tensor combine = Se2CoveringGroup.INSTANCE.combine(xya, one);
      Chop._12.requireAllZero(combine);
    }
  }

  @Test
  void testBiinvariantMeanNotTangentSpace() {
    Distribution distribution = UniformDistribution.of(-2, 2);
    Tensor vectors = RandomVariate.of(distribution, 3, 3);
    Tensor weights = RandomVariate.of(distribution, 3);
    Tensor exp = Se2CoveringGroup.INSTANCE.exponential0().exp(weights.dot(vectors));
    // ---
    Tensor sequence = Join.of( //
        Array.zeros(1, 3), //
        Tensor.of(vectors.stream().map(Se2CoveringGroup.INSTANCE.exponential0()::exp)));
    Tensor mean = Se2CoveringGroup.INSTANCE.biinvariantMean().mean(sequence, //
        Join.of(Tensors.of(RealScalar.ONE.subtract(Total.ofVector(weights))), weights));
    assertFalse(Chop._08.isClose(exp, mean));
  }

  @Test
  void testEmpty() {
    assertThrows(Exception.class, () -> Se2CoveringGroup.INSTANCE.biinvariantMean().mean(Tensors.empty(), Tensors.empty()));
  }

  @Test
  void testNonAffineFail() {
    Distribution distribution = UniformDistribution.of(-2, 2);
    Tensor sequence = RandomVariate.of(distribution, 10, 3);
    Tensor weights = RandomVariate.of(distribution, 3);
    assertThrows(Exception.class, () -> Se2CoveringGroup.INSTANCE.biinvariantMean().mean(sequence, weights));
  }
}
