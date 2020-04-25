// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class InversePowerVariogramTest extends TestCase {
  public void testSimple() {
    Tensor tensor = Tensors.vector(2, 3, 4, 5);
    Tensor w1 = NormalizeTotal.FUNCTION.apply(tensor.map(InversePowerVariogram.of(2)));
    ExactTensorQ.require(w1);
  }
}
