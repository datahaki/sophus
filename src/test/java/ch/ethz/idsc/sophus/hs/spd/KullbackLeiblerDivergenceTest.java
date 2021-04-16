// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.mat.re.Det;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class KullbackLeiblerDivergenceTest extends TestCase {
  @SuppressWarnings("unused")
  public void testSimple() {
    Tensor p = TestHelper.generateSpd(3);
    Tensor q = TestHelper.generateSpd(3);
    Chop._06.requireClose( //
        Det.of(p).divide(Det.of(q)), //
        Det.of(LinearSolve.of(q, p)));
    Scalar r1 = KullbackLeiblerDivergence.between(p, q);
    Scalar r2 = KullbackLeiblerDivergence.INSTANCE.distance(p, q);
    // System.out.println(r1);
    // System.out.println(r2);
  }
}
