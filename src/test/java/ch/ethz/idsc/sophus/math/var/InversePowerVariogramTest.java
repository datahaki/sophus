// code by jph
package ch.ethz.idsc.sophus.math.var;

import java.io.IOException;

import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import junit.framework.TestCase;

public class InversePowerVariogramTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Tensor tensor = Tensors.vector(2, 3, 4, 5);
    Tensor w1 = NormalizeTotal.FUNCTION.apply(tensor.map(Serialization.copy(InversePowerVariogram.of(2))));
    ExactTensorQ.require(w1);
  }

  public void testZero() {
    assertEquals(InversePowerVariogram.of(1).apply(RealScalar.ZERO), DoubleScalar.POSITIVE_INFINITY);
    assertEquals(InversePowerVariogram.of(2).apply(RealScalar.ZERO), DoubleScalar.POSITIVE_INFINITY);
  }

  public void testInverse() {
    assertEquals(InversePowerVariogram.of(1).apply(RealScalar.of(2)), RationalScalar.HALF);
    assertEquals(InversePowerVariogram.of(2).apply(RealScalar.of(2)), RationalScalar.of(1, 4));
  }
}
