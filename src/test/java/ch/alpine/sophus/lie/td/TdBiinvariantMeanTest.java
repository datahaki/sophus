// code by ob, jph
package ch.alpine.sophus.lie.td;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMeanTestHelper;
import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.io.Primitives;
import ch.alpine.tensor.lie.Permutations;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;

class TdBiinvariantMeanTest {
  @Test
  void testTrivial() {
    Tensor sequence = Tensors.of(Tensors.vector(2, 2));
    Tensor weights = Tensors.vector(1);
    Tensor actual = TdGroup.INSTANCE.biinvariantMean(Tolerance.CHOP).mean(sequence, weights);
    Chop._12.requireAllZero(new MeanDefect(sequence, weights, TdGroup.INSTANCE.exponential(actual)).tangent());
    assertEquals(Tensors.vector(2, 2), actual);
  }

  @Test
  void testSimple() {
    Tensor p = Tensors.vector(2, 1);
    Tensor q = Tensors.vector(3, 2);
    Tensor sequence = Tensors.of(p, q);
    Tensor weights = Tensors.vector(0.5, 0.5);
    Tensor actual = TdBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._12.requireAllZero(new MeanDefect(sequence, weights, TdGroup.INSTANCE.exponential(actual)).tangent());
    Tensor expected = Tensors.fromString("{2.414213562373095, 1.414213562373095}");
    Chop._12.requireClose(expected, actual);
  }

  @Test
  void testReorder() {
    Tensor p = Tensors.vector(2, 1);
    Tensor q = Tensors.vector(3, 2);
    Tensor r = Tensors.vector(1, 3);
    Tensor sequence = Tensors.of(p, q, r);
    Tensor mask = Tensors.vector(1, 2, 3);
    Tensor weights = mask.divide(Total.ofVector(mask));
    Tensor actual = TdBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._12.requireAllZero(new MeanDefect(sequence, weights, TdGroup.INSTANCE.exponential(actual)).tangent());
    Tensor expected = Tensors.vector(1.9243978173573888, 2.1822472719434427);
    Chop._12.requireClose(expected, actual);
    for (Tensor perm : Permutations.of(Range.of(0, weights.length()))) {
      int[] index = Primitives.toIntArray(perm);
      Tensor result = TdBiinvariantMean.INSTANCE.mean(BiinvariantMeanTestHelper.order(sequence, index), BiinvariantMeanTestHelper.order(weights, index));
      Chop._12.requireClose(result, actual);
    }
  }

  @Test
  void testReorderNegative() {
    Tensor p = Tensors.vector(2, 1);
    Tensor q = Tensors.vector(3, 2);
    Tensor r = Tensors.vector(1, 3);
    Tensor s = Tensors.vector(-1, 0.5);
    Tensor sequence = Tensors.of(p, q, r, s);
    Tensor mask = Tensors.vector(1, 2, 3, -1);
    Tensor weights = mask.divide(Total.ofVector(mask));
    Tensor actual = TdBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._12.requireAllZero(new MeanDefect(sequence, weights, TdGroup.INSTANCE.exponential(actual)).tangent());
    Tensor expected = Tensors.vector(3.1983964535982485, 2.9301560515835217);
    Chop._12.requireClose(expected, actual);
    for (Tensor perm : Permutations.of(Range.of(0, weights.length()))) {
      int[] index = Primitives.toIntArray(perm);
      Tensor result = TdBiinvariantMean.INSTANCE.mean(BiinvariantMeanTestHelper.order(sequence, index), BiinvariantMeanTestHelper.order(weights, index));
      Chop._12.requireClose(result, actual);
    }
  }

  @Test
  void testReorderNegativeVector() {
    Tensor p = Tensors.vector(2, 3, 1);
    Tensor q = Tensors.vector(3, 1, 2);
    Tensor r = Tensors.vector(1, -3, 3);
    Tensor s = Tensors.vector(4, 5, 0.5);
    Tensor sequence = Tensors.of(p, q, r, s);
    Tensor mask = Tensors.vector(1, 2, 3, -1);
    Tensor weights = mask.divide(Total.ofVector(mask));
    Tensor actual = TdBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._12.requireAllZero(new MeanDefect(sequence, weights, TdGroup.INSTANCE.exponential(actual)).tangent());
    Tensor expected = Tensors.vector(1.0099219737525793, -2.5153382244082483, 2.9301560515835217);
    Chop._12.requireClose(expected, actual);
    for (Tensor perm : Permutations.of(Range.of(0, weights.length()))) {
      int[] index = Primitives.toIntArray(perm);
      Tensor result = TdBiinvariantMean.INSTANCE.mean(BiinvariantMeanTestHelper.order(sequence, index), BiinvariantMeanTestHelper.order(weights, index));
      Chop._12.requireClose(result, actual);
    }
  }

  @Test
  void testBiinvariantMean() {
    Tensor p = Tensors.vector(2, 3, 1);
    Tensor q = Tensors.vector(3, 1, 2);
    Tensor domain = Subdivide.of(0, 1, 10);
    Tensor st1 = domain.map(TdGroup.INSTANCE.curve(p, q));
    ScalarTensorFunction mean = //
        w -> TdBiinvariantMean.INSTANCE.mean(Tensors.of(p, q), Tensors.of(RealScalar.ONE.subtract(w), w));
    Tensor st2 = domain.map(mean);
    Chop._12.requireClose(st1, st2);
  }
}
