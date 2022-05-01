// code by jph
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorNorm;
import ch.alpine.sophus.decim.LineDistance;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactScalarQ;

public class RnLineDistanceTest {
  @Test
  public void testSimple() {
    LineDistance lineDistance = RnLineDistance.INSTANCE;
    TensorNorm tensorNorm = lineDistance.tensorNorm(Tensors.vector(10, 0), Tensors.vector(10, 20));
    Scalar norm = tensorNorm.norm(Tensors.vector(30, 100));
    ExactScalarQ.require(norm);
    assertEquals(norm, RealScalar.of(20));
  }
}
