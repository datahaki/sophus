// code by jph
package ch.alpine.sophus.lie;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.sca.Chop;

class AdjointTest {
  @Test
  void testSe2() {
    Se2Group lieGroup = Se2Group.INSTANCE;
    Adjoint adjoint = new Adjoint(lieGroup, IdentityMatrix.of(3));
    Tensor g1 = Tensors.vector(1, 4, -2);
    Tensor m1 = adjoint.matrix(g1);
    Tensor g2 = Tensors.vector(7, -3, 1);
    Tensor m2 = adjoint.matrix(g2);
    Tensor g3 = lieGroup.element(g1).combine(g2);
    Tensor m3 = adjoint.matrix(g3);
    Chop._10.requireClose(m1.dot(m2), m3);
  }
}
