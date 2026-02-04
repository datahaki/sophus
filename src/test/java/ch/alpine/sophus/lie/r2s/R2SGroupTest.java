// code by jph
package ch.alpine.sophus.lie.r2s;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

class R2SGroupTest {
  @Test
  void testSimple() {
    Tensor xyu = RandomVariate.of(NormalDistribution.standard(), 3);
    Tolerance.CHOP.requireAllZero(R2SGroup.INSTANCE.diffOp(xyu).apply(xyu));
    Tolerance.CHOP.requireAllZero(R2SGroup.INSTANCE.combine(xyu, R2SGroup.INSTANCE.invert(xyu)));
  }

  @Test
  void test2Pi() {
    Tensor p = Tensors.of(RealScalar.ZERO, RealScalar.ZERO, Pi.VALUE);
    Tolerance.CHOP.requireAllZero(R2SGroup.INSTANCE.combine(p, p));
  }
}
