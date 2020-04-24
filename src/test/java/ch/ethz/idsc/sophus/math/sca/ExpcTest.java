// code by jph
package ch.ethz.idsc.sophus.math.sca;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class ExpcTest extends TestCase {
  public void testSimple() {
    Scalar scalar = Expc.FUNCTION.apply(RealScalar.of(-1e-13));
    Chop._02.requireClose(scalar, RealScalar.ONE);
  }

  public void testEps() {
    Scalar scalar = Expc.FUNCTION.apply(RealScalar.of(Double.MIN_VALUE));
    Tolerance.CHOP.requireClose(scalar, RealScalar.ONE);
  }

  public void testRandom() {
    Distribution distribution = UniformDistribution.of(0, 2e-12);
    for (int count = 0; count < 100; ++count) {
      Scalar mu = RandomVariate.of(distribution);
      Tolerance.CHOP.requireClose( //
          Expc.FUNCTION.apply(mu), //
          RealScalar.ONE);
    }
  }
}
