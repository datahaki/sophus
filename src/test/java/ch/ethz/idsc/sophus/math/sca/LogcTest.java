// code by jph
package ch.ethz.idsc.sophus.math.sca;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Exp;
import junit.framework.TestCase;

public class LogcTest extends TestCase {
  public void testSimple() {
    Scalar scalar = Logc.FUNCTION.apply(RealScalar.of(1 + 1e-13));
    Chop._08.requireClose(scalar, RealScalar.ONE);
  }

  public void testFraction() {
    Scalar dl1 = RandomVariate.of(NormalDistribution.standard());
    Scalar dl2 = RandomVariate.of(NormalDistribution.standard());
    Scalar l1 = Exp.FUNCTION.apply(dl1);
    Scalar l2 = Exp.FUNCTION.apply(dl2);
    Scalar lhs = Logc.FUNCTION.apply(l1.divide(l2));
    Scalar rhs = dl1.subtract(dl2).divide(l1.divide(l2).subtract(RealScalar.ONE));
    Tolerance.CHOP.requireClose(lhs, rhs);
  }
}
