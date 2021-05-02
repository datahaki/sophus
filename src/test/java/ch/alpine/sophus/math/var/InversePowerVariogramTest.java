// code by jph
package ch.alpine.sophus.math.var;

import java.io.IOException;

import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.nrm.NormalizeTotal;
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

  public void testExponentZero() throws ClassNotFoundException, IOException {
    ScalarUnaryOperator suo = Serialization.copy(InversePowerVariogram.of(0));
    Tensor domain = Subdivide.of(-1, 1, 6);
    assertEquals(domain.map(suo), ConstantArray.of(RealScalar.ONE, 7));
  }
}
