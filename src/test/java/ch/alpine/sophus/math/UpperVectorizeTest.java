// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Partition;
import ch.alpine.tensor.alg.Range;

class UpperVectorizeTest {
  @Test
  void test0() {
    Tensor matrix = Partition.of(Range.of(1, 10), 3);
    Tensor res = UpperVectorize.of(matrix, 0);
    assertEquals(res, Tensors.vector(1, 2, 3, 5, 6, 9));
  }

  @Test
  void test1() {
    Tensor matrix = Partition.of(Range.of(1, 10), 3);
    Tensor res = UpperVectorize.of(matrix, 1);
    assertEquals(res, Tensors.vector(2, 3, 6));
  }

  @Test
  void test2() {
    Tensor matrix = Partition.of(Range.of(1, 10), 3);
    Tensor res = UpperVectorize.of(matrix, 2);
    assertEquals(res, Tensors.vector(3));
  }
}
