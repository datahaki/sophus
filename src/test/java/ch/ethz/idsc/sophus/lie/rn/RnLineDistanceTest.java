// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.decim.LineDistance;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensors;
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
