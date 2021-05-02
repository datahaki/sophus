// code by jph
package ch.alpine.sophus.hs.spd;

import java.lang.reflect.Modifier;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.MatrixLog;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.red.Trace;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Log;
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
