// code by jph
package ch.alpine.sophus.lie.so3;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.sophus.lie.gl.GlGroupElement;
import ch.alpine.sophus.lie.so.SoGroupElement;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.LinearSolve;

class So3ExponentialTest {
  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    Serialization.copy(So3Group.INSTANCE);
  }

  @Test
  public void testAdjoint() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = So3TestHelper.spawn_So3();
      Tensor v = So3TestHelper.spawn_so3();
      Tensor tensor = LinearSolve.of(g, v).dot(g);
      AntisymmetricMatrixQ.require(tensor);
      // AntisymmetricMatrixQ.require(So3Exponential.INSTANCE.log(g));
      VectorQ.requireLength(So3Group.INSTANCE.vectorLog(g), 3);
    }
  }

  @Test
  public void testLinearGroup() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = So3TestHelper.spawn_So3();
      SoGroupElement so3GroupElement = So3Group.INSTANCE.element(g);
      GlGroupElement linearGroupElement = GlGroup.INSTANCE.element(g);
      Tensor v = So3TestHelper.spawn_so3();
      Tolerance.CHOP.requireClose( //
          so3GroupElement.adjoint(v), //
          linearGroupElement.adjoint(v));
      Tolerance.CHOP.requireClose( //
          so3GroupElement.dL(v), //
          linearGroupElement.dL(v));
    }
  }

  @Test
  public void testFailOrthogonal() {
    assertThrows(Exception.class, () -> So3Group.INSTANCE.log(So3TestHelper.spawn_so3()));
  }
}
