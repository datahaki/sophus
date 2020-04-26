// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.Serialization;
import junit.framework.TestCase;

public class InversePowerVariogramTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Tensor tensor = Tensors.vector(2, 3, 4, 5);
    Tensor w1 = NormalizeTotal.FUNCTION.apply(tensor.map(Serialization.copy(InversePowerVariogram.of(2))));
    ExactTensorQ.require(w1);
  }
}
