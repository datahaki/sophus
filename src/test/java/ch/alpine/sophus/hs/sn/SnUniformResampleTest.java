// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.sophus.math.AdjacentDistances;
import ch.alpine.sophus.ref.d1.CurveSubdivision;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.red.Variance;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnUniformResampleTest extends TestCase {
  public void testSimple() {
    Scalar spacing = Pi.VALUE.divide(RealScalar.of(10));
    CurveSubdivision curveSubdivision = SnUniformResample.of(spacing);
    Tensor tensor = Tensors.fromString("{{1, 0}, {0, 1}, {-1, 0}}");
    Tensor string = curveSubdivision.string(tensor);
    Tensor distances = new AdjacentDistances(SnMetric.INSTANCE).apply(string);
    Scalar variance = Variance.ofVector(distances);
    Chop._20.requireAllZero(variance);
  }
}
