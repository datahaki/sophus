// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.decim.LineDistance;
import ch.alpine.sophus.math.TensorNorm;
import ch.alpine.tensor.ExactScalarQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class RnLineDistanceTest extends TestCase {
  public void testSimple() {
    LineDistance lineDistance = RnLineDistance.INSTANCE;
    TensorNorm tensorNorm = lineDistance.tensorNorm(Tensors.vector(10, 0), Tensors.vector(10, 20));
    Scalar norm = tensorNorm.norm(Tensors.vector(30, 100));
    ExactScalarQ.require(norm);
    assertEquals(norm, RealScalar.of(20));
  }
}
