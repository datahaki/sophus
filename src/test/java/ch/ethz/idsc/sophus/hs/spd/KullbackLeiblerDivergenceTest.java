// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class KullbackLeiblerDivergenceTest extends TestCase {
  public void testSimple() {
    Tensor p = TestHelper.generateSpd(3);
    Tensor q = TestHelper.generateSpd(3);
    Chop._06.requireClose( //
        Det.of(p).divide(Det.of(q)), //
        Det.of(LinearSolve.of(q, p)));
  }
}
