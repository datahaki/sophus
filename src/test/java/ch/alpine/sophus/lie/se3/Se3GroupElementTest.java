// code by jph
package ch.alpine.sophus.lie.se3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.so3.Rodrigues;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.sca.Chop;

public class Se3GroupElementTest {
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

  @Test
  public void testSimple() {
    Tensor R = Rodrigues.vectorExp(Tensors.vector(-1, -.2, .3));
    Tensor t = Tensors.vector(4, 5, 6);
    _check(R, t);
    _check(IdentityMatrix.of(3), Array.zeros(3));
  }
}
