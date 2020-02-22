// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.lie.BiinvariantMeanTestHelper;
import ch.ethz.idsc.sophus.lie.LieGroupTests;
import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.io.Primitives;
import ch.ethz.idsc.tensor.lie.Permutations;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.qty.QuantityMagnitude;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import junit.framework.TestCase;

public class Se2CoveringBiinvariantMeanTest extends TestCase {
  public void testPermutations() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int length = 1; length < 6; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor weights = RandomVariate.of(distribution, length);
      weights = weights.divide(Total.ofVector(weights));
      Tensor solution = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
      for (Tensor perm : Permutations.of(Range.of(0, weights.length()))) {
        int[] index = Primitives.toIntArray(perm);
        Tensor result = Se2CoveringBiinvariantMean.INSTANCE.mean(BiinvariantMeanTestHelper.order(sequence, index),
            BiinvariantMeanTestHelper.order(weights, index));
        Chop._03.requireClose(result, solution);
      }
    }
  }

  public void testEquation() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int length = 1; length < 6; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor weights = RandomVariate.of(distribution, length);
      weights = weights.divide(Total.ofVector(weights));
      Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
      Tensor defect = Se2CoveringBiinvariantMeanDefect.INSTANCE.defect(sequence, weights, mean);
      Chop._06.requireClose(defect, defect.map(Scalar::zero));
    }
  }

  public void testEquationQuantity() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int length = 1; length < 6; ++length) {
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, length, 3).stream().map(row -> Tensors.of( //
          Quantity.of(row.Get(0), "m"), //
          Quantity.of(row.Get(1), "m"), //
          Quantity.of(row.Get(2), ""))));
      Tensor weights = RandomVariate.of(distribution, length);
      weights = weights.divide(Total.ofVector(weights));
      Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
      QuantityMagnitude.SI().in("m").apply(mean.Get(0));
      QuantityMagnitude.SI().in("m").apply(mean.Get(1));
      Tensor defect = Se2CoveringBiinvariantMeanDefect.INSTANCE.defect(sequence, weights, mean);
      Chop._06.requireClose(defect, defect.map(Scalar::zero));
    }
  }

  public void testIdentityXY() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(8));
    for (int length = 1; length < 6; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      sequence.set(s -> RealScalar.ZERO, Tensor.ALL, 2);
      Tensor weights = RandomVariate.of(distribution, length);
      weights = weights.divide(Total.ofVector(weights));
      Tensor solution = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
      for (Tensor perm : Permutations.of(Range.of(0, weights.length()))) {
        int[] index = Primitives.toIntArray(perm);
        Tensor result = Se2CoveringBiinvariantMean.INSTANCE.mean(BiinvariantMeanTestHelper.order(sequence, index),
            BiinvariantMeanTestHelper.order(weights, index));
        Chop._08.requireClose(result, solution);
        Tensor rnmean = RnBiinvariantMean.INSTANCE.mean(BiinvariantMeanTestHelper.order(sequence, index), BiinvariantMeanTestHelper.order(weights, index));
        Chop._02.requireClose(result, rnmean);
      }
    }
  }

  public void testRandom() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 4; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 3);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, n));
      Tensor xya = Se2CoveringBiinvariantMean.INSTANCE.mean(points, weights);
      Tensor seqinv = LieGroupTests.invert(Se2CoveringGroup.INSTANCE, points);
      Tensor xyainv = Se2CoveringBiinvariantMean.INSTANCE.mean(seqinv, weights);
      Tensor combine = Se2CoveringGroup.INSTANCE.element(xya).combine(xyainv);
      Chop._12.requireAllZero(combine);
    }
  }

  public void testBiinvariantMeanNotTangentSpace() {
    Distribution distribution = UniformDistribution.of(-2, 2);
    Tensor vectors = RandomVariate.of(distribution, 3, 3);
    Tensor weights = RandomVariate.of(distribution, 3);
    Tensor exp = Se2CoveringExponential.INSTANCE.exp(weights.dot(vectors));
    // ---
    Tensor sequence = Join.of( //
        Array.zeros(1, 3), //
        Tensor.of(vectors.stream().map(Se2CoveringExponential.INSTANCE::exp)));
    Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, //
        Join.of(Tensors.of(RealScalar.ONE.subtract(Total.ofVector(weights))), weights));
    assertFalse(Chop._08.close(exp, mean));
  }

  public void testEmpty() {
    try {
      Se2CoveringBiinvariantMean.INSTANCE.mean(Tensors.empty(), Tensors.empty());
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
