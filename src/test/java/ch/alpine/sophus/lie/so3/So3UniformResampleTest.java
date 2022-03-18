// code by jph
package ch.alpine.sophus.lie.so3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.ref.d1.CurveSubdivision;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;

public class So3UniformResampleTest {
  @Test
  public void testSimple() {
    CurveSubdivision curveSubdivision = So3UniformResample.of(RealScalar.ONE);
    Tensor vector = Tensors.vector(l -> So3TestHelper.spawn_So3(), 20);
    Tensor string = curveSubdivision.string(vector);
    assertEquals(Dimensions.of(string).subList(1, 3), Arrays.asList(3, 3));
  }
}
