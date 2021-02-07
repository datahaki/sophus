// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.gln.GlnGroup;
import ch.ethz.idsc.sophus.lie.gln.GlnGroupElement;
import ch.ethz.idsc.sophus.lie.son.SonGroup;
import ch.ethz.idsc.sophus.lie.son.SonGroupElement;
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
      SonGroupElement so3GroupElement = SonGroup.INSTANCE.element(g);
      GlnGroupElement linearGroupElement = GlnGroup.INSTANCE.element(g);
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
