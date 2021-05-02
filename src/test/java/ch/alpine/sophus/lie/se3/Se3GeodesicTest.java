// code by jph
package ch.alpine.sophus.lie.se3;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.MatrixQ;
import junit.framework.TestCase;

public class Se3GeodesicTest extends TestCase {
  public void testSimple() {
    Tensor p = Se3Exponential.INSTANCE.exp(Tensors.of(Tensors.vector(1, 2, 3), Tensors.vector(0.2, 0.3, 0.4)));
    Tensor q = Se3Exponential.INSTANCE.exp(Tensors.of(Tensors.vector(3, 4, 5), Tensors.vector(-0.1, 0.2, 0.2)));
    Tensor split = Se3Geodesic.INSTANCE.split(p, q, RationalScalar.HALF);
    assertTrue(MatrixQ.ofSize(split, 4, 4));
  }
}