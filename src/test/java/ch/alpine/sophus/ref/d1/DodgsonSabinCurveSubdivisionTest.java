// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.sca.Chop;

public class DodgsonSabinCurveSubdivisionTest {
  @Test
  public void testThree() {
    Tensor a = Tensors.vector(1, 1.1);
    Tensor b = Tensors.vector(2.2, 0.5);
    Tensor c = Tensors.vector(3, 1.5);
    Tensor tensor = Tensors.of(a, b, c);
    Tensor string = DodgsonSabinCurveSubdivision.INSTANCE.string(tensor);
    assertEquals(Dimensions.of(string), Arrays.asList(5, 2));
    assertEquals(string.get(0), a);
    assertEquals(string.get(2), b);
    assertEquals(string.get(4), c);
  }

  @Test
  public void testFour() {
    Tensor a = Tensors.vector(1, 1.1);
    Tensor b = Tensors.vector(2.2, 0.5);
    Tensor c = Tensors.vector(3, 1.5);
    Tensor d = Tensors.vector(3.5, 2.9);
    Tensor tensor = Tensors.of(a, b, c, d);
    Tensor string = DodgsonSabinCurveSubdivision.INSTANCE.string(tensor);
    assertEquals(Dimensions.of(string), Arrays.asList(7, 2));
    assertEquals(string.get(0), a);
    assertEquals(string.get(2), b);
    assertEquals(string.get(4), c);
    assertEquals(string.get(6), d);
    Chop._13.requireClose(string.get(3), //
        Tensors.fromString("{2.64619661516195, 0.8388990046231528}"));
  }

  @Test
  public void testCyclic() {
    for (int n = 3; n < 10; ++n)
      Chop._13.requireClose( //
          DodgsonSabinCurveSubdivision.INSTANCE.cyclic(CirclePoints.of(n)), //
          CirclePoints.of(2 * n));
  }
}
