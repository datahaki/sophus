// code by jph
package ch.alpine.sophus.lie.so3;

import java.io.IOException;

import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.sophus.lie.gl.GlGroupElement;
import ch.alpine.sophus.lie.so.SoGroup;
import ch.alpine.sophus.lie.so.SoGroupElement;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.LinearSolve;
import junit.framework.TestCase;

public class So3ExponentialTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Serialization.copy(So3Exponential.INSTANCE);
  }

  public void testAdjoint() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = So3TestHelper.spawn_So3();
      Tensor v = So3TestHelper.spawn_so3();
      Tensor tensor = LinearSolve.of(g, v).dot(g);
      AntisymmetricMatrixQ.require(tensor);
      // AntisymmetricMatrixQ.require(So3Exponential.INSTANCE.log(g));
      VectorQ.requireLength(So3Exponential.INSTANCE.vectorLog(g), 3);
    }
  }

  public void testLinearGroup() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = So3TestHelper.spawn_So3();
      SoGroupElement so3GroupElement = SoGroup.INSTANCE.element(g);
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

  public void testFailOrthogonal() {
    AssertFail.of(() -> So3Exponential.INSTANCE.log(So3TestHelper.spawn_so3()));
  }
}
