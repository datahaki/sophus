// code by jph
package ch.alpine.sophus.hs.spd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.red.Trace;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.exp.Log;

public class StaticHelperTest {
  @Test
  public void testSimple() {
    assertEquals(StaticHelper.norm(IdentityMatrix.of(3)), RealScalar.ZERO);
  }

  @Test
  public void testTrace() {
    RandomSampleInterface rsi = new Spd0RandomSample(3, TriangularDistribution.with(0, 1));
    Tensor q = RandomSample.of(rsi);
    Tensor diag = Tensor.of(Eigensystem.ofSymmetric(q).values().stream() //
        .map(Scalar.class::cast)) //
        .map(Log.FUNCTION);
    Tensor log = MatrixLog.of(q);
    Chop._07.requireClose(Total.ofVector(diag), Trace.of(log));
    Tensor log2 = log.dot(log);
    Chop._07.requireClose(Vector2NormSquared.of(diag), Trace.of(log2));
  }

  @Test
  public void testPackageVisibility() {
    assertFalse(Modifier.isPublic(StaticHelper.class.getModifiers()));
  }
}
