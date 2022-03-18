// code by jph
package ch.alpine.sophus.hs.sn;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class SnPerturbationTest {
  @Test
  public void testSimple() {
    TensorUnaryOperator snPerturbation = SnPerturbation.of(NormalDistribution.standard());
    Tensor p = UnitVector.of(3, 1);
    Tensor q = snPerturbation.apply(p);
    SnMemberQ.INSTANCE.require(q);
  }
}
