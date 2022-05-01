// code by jph
package ch.alpine.sophus.lie.se3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.so3.Rodrigues;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;

class Se3DifferencesTest {
  @Test
  public void testSimple() {
    Tensor m1 = Se3Matrix.of(Rodrigues.vectorExp(Tensors.vector(0.2, -0.3, 0.4)), Tensors.vector(10, 20, 30));
    Tensor m2 = Se3Matrix.of(Rodrigues.vectorExp(Tensors.vector(-0.2, 0.3, 0.4)), Tensors.vector(11, 21, 31));
    Tensor tensor = Se3Differences.INSTANCE.apply(Tensors.of(m1, m2));
    assertEquals(Dimensions.of(tensor), Arrays.asList(1, 2, 3));
  }
}
