// code by jph
package ch.alpine.sophus.hs.r3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;

class PlatonicSolidTest {
  @ParameterizedTest
  @EnumSource
  void test(PlatonicSolid platonicSolid) {
    Tensor faces = Tensors.matrixInt(platonicSolid.faces());
    assertEquals(Dimensions.of(faces), List.of(platonicSolid.n(), platonicSolid.shape()));
  }
}
