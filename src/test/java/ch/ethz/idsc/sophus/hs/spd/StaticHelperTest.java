// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import java.lang.reflect.Modifier;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.ev.Eigensystem;
import ch.ethz.idsc.tensor.nrm.Vector2NormSquared;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.red.Trace;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Log;
import junit.framework.TestCase;

public class StaticHelperTest extends TestCase {
  public void testSimple() {
    assertEquals(StaticHelper.norm(IdentityMatrix.of(3)), RealScalar.ZERO);
  }

  public void testTrace() {
    Tensor q = TestHelper.generateSpd(3);
    Tensor diag = Tensor.of(Eigensystem.ofSymmetric(q).values().stream() //
        .map(Scalar.class::cast)) //
        .map(Log.FUNCTION);
    Tensor log = MatrixLog.of(q);
    Chop._07.requireClose(Total.ofVector(diag), Trace.of(log));
    Tensor log2 = log.dot(log);
    Chop._07.requireClose(Vector2NormSquared.of(diag), Trace.of(log2));
  }

  public void testPackageVisibility() {
    assertFalse(Modifier.isPublic(StaticHelper.class.getModifiers()));
  }
}
