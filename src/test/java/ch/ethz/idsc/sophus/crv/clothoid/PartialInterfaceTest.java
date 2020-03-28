// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class PartialInterfaceTest extends TestCase {
  public void testSimple1() {
    PartialInterface partialInterface = PartialInterface.of(RealScalar.of(1.3), RealScalar.of(-0.7), RealScalar.of(0.2));
    Scalar result = partialInterface.il(RealScalar.of(0.3));
    Scalar expect = ComplexScalar.of(0.1082616101141145, 0.27929038997053457);
    Tolerance.CHOP.requireClose(result, expect);
  }

  public void testSimple2() {
    PartialInterface partialInterface = PartialInterface.of(RealScalar.of(1.5), RealScalar.of(0.3), RealScalar.of(-0.4));
    Scalar result = partialInterface.il(RealScalar.of(0.4));
    Scalar expect = ComplexScalar.of(0.012847600349171472, 0.39973677660732543);
    Tolerance.CHOP.requireClose(result, expect);
  }

  public void testCircle() {
    PartialInterface partialInterface = PartialInterface.of(RealScalar.of(1.3), RealScalar.of(-0.7), RealScalar.of(0));
    Scalar result = partialInterface.il(RealScalar.of(0.3));
    Scalar expect = ComplexScalar.of(0.10990181566815083, 0.2785521975010192);
    Tolerance.CHOP.requireClose(result, expect);
  }

  public void testLine() {
    PartialInterface partialInterface = PartialInterface.of(RealScalar.of(1.3), RealScalar.of(0), RealScalar.of(0));
    Scalar result = partialInterface.il(RealScalar.of(0.3));
    Scalar expect = ComplexScalar.of(0.0802496485873762, 0.2890674556251579);
    Tolerance.CHOP.requireClose(result, expect);
  }
}