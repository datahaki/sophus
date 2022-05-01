// code by jph
package ch.alpine.sophus.lie.so2;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.ref.d1.CurveSubdivision;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Differences;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.red.Variance;
import ch.alpine.tensor.sca.Chop;

class So2UniformResampleTest {
  @Test
  public void testSimple() {
    CurveSubdivision curveSubdivision = So2UniformResample.of(RealScalar.of(0.1));
    Tensor tensor = curveSubdivision.string(Subdivide.of(0, 10, 20));
    Scalar variance = Variance.ofVector(Differences.of(tensor));
    Chop._20.requireZero(variance);
  }
}
