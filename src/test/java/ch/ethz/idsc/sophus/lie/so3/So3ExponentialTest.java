// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.gl.LinearGroup;
import ch.ethz.idsc.sophus.lie.gl.LinearGroupElement;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.AntisymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class So3ExponentialTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Serialization.copy(new So3Exponential(TestHelper.spawn_So3()));
  }

  public void testAdjoint() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = TestHelper.spawn_So3();
      Tensor v = TestHelper.spawn_so3();
      Tensor tensor = LinearSolve.of(g, v).dot(g);
      AntisymmetricMatrixQ.require(tensor);
    }
  }

  public void testLinearGroup() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = TestHelper.spawn_So3();
      So3GroupElement so3GroupElement = So3Group.INSTANCE.element(g);
      LinearGroupElement linearGroupElement = LinearGroup.INSTANCE.element(g);
      Tensor v = TestHelper.spawn_so3();
      Tolerance.CHOP.requireClose( //
          so3GroupElement.adjoint(v), //
          linearGroupElement.adjoint(v));
      Tolerance.CHOP.requireClose( //
          so3GroupElement.dL(v), //
          linearGroupElement.dL(v));
    }
  }

  public void testFailOrthogonal() {
    try {
      new So3Exponential(TestHelper.spawn_so3());
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
