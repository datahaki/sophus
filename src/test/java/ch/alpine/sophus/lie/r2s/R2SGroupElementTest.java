// code by jph
package ch.alpine.sophus.lie.r2s;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
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
