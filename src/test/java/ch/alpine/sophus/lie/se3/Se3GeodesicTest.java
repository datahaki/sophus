// code by jph
package ch.alpine.sophus.lie.se3;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.MatrixQ;

class Se3GeodesicTest {
  @Test
  public void testSimple() {
    Tensor p = Se3Group.INSTANCE.exp(Tensors.of(Tensors.vector(1, 2, 3), Tensors.vector(0.2, 0.3, 0.4)));
    Tensor q = Se3Group.INSTANCE.exp(Tensors.of(Tensors.vector(3, 4, 5), Tensors.vector(-0.1, 0.2, 0.2)));
    Tensor split = Se3Group.INSTANCE.split(p, q, RationalScalar.HALF);
    assertTrue(MatrixQ.ofSize(split, 4, 4));
  }
}
