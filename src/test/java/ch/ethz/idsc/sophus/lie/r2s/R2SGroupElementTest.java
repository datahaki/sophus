// code by jph
package ch.ethz.idsc.sophus.lie.r2s;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class R2SGroupElementTest extends TestCase {
  public void testSimple() {
    Tensor xyu = RandomVariate.of(NormalDistribution.standard(), 3);
    R2SGroupElement r2s = new R2SGroupElement(xyu);
    Tolerance.CHOP.requireAllZero(r2s.inverse().combine(xyu));
    Tolerance.CHOP.requireAllZero(r2s.combine(r2s.inverse().toCoordinate()));
  }

  public void test2Pi() {
    R2SGroupElement r2s = new R2SGroupElement(Tensors.of(RealScalar.ZERO, RealScalar.ZERO, Pi.VALUE));
    Tensor result = r2s.combine(Tensors.of(RealScalar.ZERO, RealScalar.ZERO, Pi.VALUE));
    Tolerance.CHOP.requireAllZero(result);
  }
}
