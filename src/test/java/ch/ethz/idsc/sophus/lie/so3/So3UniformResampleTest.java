// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import java.util.Arrays;

import ch.ethz.idsc.sophus.ref.d1.CurveSubdivision;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dimensions;
import junit.framework.TestCase;

public class So3UniformResampleTest extends TestCase {
  public void testSimple() {
    CurveSubdivision curveSubdivision = So3UniformResample.of(RealScalar.ONE);
    Tensor vector = Tensors.vector(l -> So3TestHelper.spawn_So3(), 20);
    Tensor string = curveSubdivision.string(vector);
    assertEquals(Dimensions.of(string).subList(1, 3), Arrays.asList(3, 3));
  }
}
