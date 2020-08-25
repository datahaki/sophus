// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import java.util.Arrays;

import ch.ethz.idsc.sophus.lie.so3.Rodrigues;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se3GroupElementTest extends TestCase {
  private static void _check(Tensor R, Tensor t) {
    Se3GroupElement g = new Se3GroupElement(R, t);
    Tensor adjoint = g.inverse().adjoint(Tensors.fromString("{{1, 2, 3}, {4, 5, 6}}"));
    assertEquals(Dimensions.of(adjoint), Arrays.asList(2, 3));
    Tensor ge = g.combine(IdentityMatrix.of(4));
    Chop._10.requireClose(Se3Matrix.rotation(ge), R);
    Chop._10.requireClose(Se3Matrix.rotation(ge), g.rotation());
    Chop._10.requireClose(Se3Matrix.translation(ge), t);
    Chop._10.requireClose(Se3Matrix.translation(ge), g.translation());
  }

  public void testSimple() {
    Tensor R = Rodrigues.vectorExp(Tensors.vector(-1, -.2, .3));
    Tensor t = Tensors.vector(4, 5, 6);
    _check(R, t);
    _check(IdentityMatrix.of(3), Array.zeros(3));
  }
}
