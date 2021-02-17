// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import junit.framework.TestCase;

public class SnPerturbationTest extends TestCase {
  public void testSimple() {
    TensorUnaryOperator snPerturbation = SnPerturbation.of(NormalDistribution.standard());
    Tensor p = UnitVector.of(3, 1);
    Tensor q = snPerturbation.apply(p);
    SnMemberQ.INSTANCE.require(q);
  }
}
