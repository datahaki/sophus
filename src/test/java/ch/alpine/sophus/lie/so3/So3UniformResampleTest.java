// code by jph
package ch.alpine.sophus.lie.so3;

import java.util.Arrays;

import ch.alpine.sophus.ref.d1.CurveSubdivision;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import junit.framework.TestCase;

public class So3UniformResampleTest extends TestCase {
  public void testSimple() {
    CurveSubdivision curveSubdivision = So3UniformResample.of(RealScalar.ONE);
    Tensor vector = Tensors.vector(l -> So3TestHelper.spawn_So3(), 20);
    Tensor string = curveSubdivision.string(vector);
    assertEquals(Dimensions.of(string).subList(1, 3), Arrays.asList(3, 3));
  }
}
