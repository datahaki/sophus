// code by ob, jph
package ch.alpine.sophus.lie.dt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMeanTestHelper;
import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.io.Primitives;
import ch.alpine.tensor.lie.Permutations;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;

class DtBiinvariantMeanTest {
  @Test
  public void testTrivial() {
    Tensor sequence = Tensors.of(Tensors.vector(2, 2));
    Tensor weights = Tensors.vector(1);
    Tensor actual = DtBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._12.requireAllZero(new MeanDefect(sequence, weights, DtGroup.INSTANCE.exponential(actual)).tangent());
    assertEquals(Tensors.vector(2, 2), actual);
  }

  @Test
  public void testSimple() {
    Tensor p = Tensors.vector(1, 2);
    Tensor q = Tensors.vector(2, 3);
    Tensor sequence = Tensors.of(p, q);
    Tensor weights = Tensors.vector(0.5, 0.5);
    Tensor actual = DtBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._12.requireAllZero(new MeanDefect(sequence, weights, DtGroup.INSTANCE.exponential(actual)).tangent());
    Tensor expected = Tensors.fromString("{1.414213562373095, 2.414213562373095}");
    Chop._12.requireClose(expected, actual);
  }

  @Test
  public void testReorder() {
    Tensor p = Tensors.vector(1, 2);
    Tensor q = Tensors.vector(2, 3);
    Tensor r = Tensors.vector(3, 1);
    Tensor sequence = Tensors.of(p, q, r);
    Tensor mask = Tensors.vector(1, 2, 3);
    Tensor weights = mask.divide(Total.ofVector(mask));
    Tensor actual = DtBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._12.requireAllZero(new MeanDefect(sequence, weights, DtGroup.INSTANCE.exponential(actual)).tangent());
    Tensor expected = Tensors.vector(2.1822472719434427, 1.9243978173573888);
    Chop._12.requireClose(expected, actual);
    for (Tensor perm : Permutations.of(Range.of(0, weights.length()))) {
      int[] index = Primitives.toIntArray(perm);
      Tensor result = DtBiinvariantMean.INSTANCE.mean(BiinvariantMeanTestHelper.order(sequence, index), BiinvariantMeanTestHelper.order(weights, index));
      Chop._12.requireClose(result, actual);
    }
  }

  @Test
  public void testReorderNegative() {
    Tensor p = Tensors.vector(1, 2);
    Tensor q = Tensors.vector(2, 3);
    Tensor r = Tensors.vector(3, 1);
    Tensor s = Tensors.vector(0.5, -1);
    Tensor sequence = Tensors.of(p, q, r, s);
    Tensor mask = Tensors.vector(1, 2, 3, -1);
    Tensor weights = mask.divide(Total.ofVector(mask));
    Tensor actual = DtBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._12.requireAllZero(new MeanDefect(sequence, weights, DtGroup.INSTANCE.exponential(actual)).tangent());
    Tensor expected = Tensors.vector(2.9301560515835217, 3.1983964535982485);
    Chop._12.requireClose(expected, actual);
    for (Tensor perm : Permutations.of(Range.of(0, weights.length()))) {
      int[] index = Primitives.toIntArray(perm);
      Tensor result = DtBiinvariantMean.INSTANCE.mean(BiinvariantMeanTestHelper.order(sequence, index), BiinvariantMeanTestHelper.order(weights, index));
      Chop._12.requireClose(result, actual);
    }
  }

  @Test
  public void testReorderNegativeVector() {
    Tensor p = Tensors.fromString("{1, {2, 3}}");
    Tensor q = Tensors.fromString("{2, {3, 1}}");
    Tensor r = Tensors.fromString("{3, {1, -3}}");
    Tensor s = Tensors.fromString("{0.5, {4, 5}}");
    Tensor sequence = Tensors.of(p, q, r, s);
    Tensor mask = Tensors.vector(1, 2, 3, -1);
    Tensor weights = mask.divide(Total.ofVector(mask));
    Tensor actual = DtBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._12.requireAllZero(new MeanDefect(sequence, weights, DtGroup.INSTANCE.exponential(actual)).tangent());
    Tensor expected = Tensors.fromString("{2.9301560515835217, {1.0099219737525793, -2.5153382244082483}}");
    Chop._12.requireClose(expected, actual);
    for (Tensor perm : Permutations.of(Range.of(0, weights.length()))) {
      int[] index = Primitives.toIntArray(perm);
      Tensor result = DtBiinvariantMean.INSTANCE.mean(BiinvariantMeanTestHelper.order(sequence, index), BiinvariantMeanTestHelper.order(weights, index));
      Chop._12.requireClose(result, actual);
    }
  }
}
