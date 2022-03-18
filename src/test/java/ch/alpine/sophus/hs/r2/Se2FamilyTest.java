// code by jph
package ch.alpine.sophus.hs.r2;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.BijectionFamily;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

public class Se2FamilyTest {
  @Test
  public void testSimple() {
    BijectionFamily bijectionFamily = new Se2Family(s -> Tensors.of( //
        RealScalar.of(2).add(s), //
        RealScalar.of(1).multiply(s), RealScalar.of(5).subtract(s) //
    ));
    Distribution distribution = NormalDistribution.standard();
    for (int index = 0; index < 100; ++index) {
      Scalar scalar = RandomVariate.of(distribution);
      Tensor point = RandomVariate.of(distribution, 2);
      Tensor fwd = bijectionFamily.forward(scalar).apply(point);
      Chop._14.requireClose(bijectionFamily.inverse(scalar).apply(fwd), point);
    }
  }

  @Test
  public void testReverse() {
    BijectionFamily bijectionFamily = new Se2Family(s -> Tensors.of( //
        RealScalar.of(2).add(s), //
        RealScalar.of(1).multiply(s), RealScalar.of(5).subtract(s) //
    ));
    Distribution distribution = NormalDistribution.standard();
    for (int index = 0; index < 100; ++index) {
      Scalar scalar = RandomVariate.of(distribution);
      Tensor point = RandomVariate.of(distribution, 2);
      Tensor fwd = bijectionFamily.inverse(scalar).apply(point);
      Chop._14.requireClose(bijectionFamily.forward(scalar).apply(fwd), point);
    }
  }
}
