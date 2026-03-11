// code by jph
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.MatrixQ;
import ch.alpine.tensor.num.Pi;

class RExponentialTest {
  @Test
  void test() {
    Tensor matrix = RExponential.INSTANCE.gl_representation(Pi.VALUE);
    assertTrue(MatrixQ.ofSize(matrix, 2, 2));
  }
}
