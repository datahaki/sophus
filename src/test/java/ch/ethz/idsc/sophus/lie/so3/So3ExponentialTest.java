// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.gl.GlGroup;
import ch.ethz.idsc.sophus.lie.gl.GlGroupElement;
import ch.ethz.idsc.sophus.lie.so.SoGroup;
import ch.ethz.idsc.sophus.lie.so.SoGroupElement;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.mat.AntisymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.mat.Tolerance;
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
