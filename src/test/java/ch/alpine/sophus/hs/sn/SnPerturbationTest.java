// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.pdf.NormalDistribution;
import junit.framework.TestCase;

public class SnPerturbationTest extends TestCase {
  public void testSimple() {
    TensorUnaryOperator snPerturbation = SnPerturbation.of(NormalDistribution.standard());
    Tensor p = UnitVector.of(3, 1);
    Tensor q = snPerturbation.apply(p);
    SnMemberQ.INSTANCE.require(q);
  }
}
