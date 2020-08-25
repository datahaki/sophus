// code by jph
package ch.ethz.idsc.sophus.hs.s2;

import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class S2ExponentialTest extends TestCase {
  public void testSimple() {
    S2Exponential s2Exponential = new S2Exponential(Tensors.vector(1, 0, 0));
    Tensor tensor = s2Exponential.projection();
    ExactTensorQ.require(tensor);
    assertEquals(tensor, IdentityMatrix.of(3).extract(1, 3));
  }
}
