// code by jph
package ch.alpine.sophus.hs.r3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ch.alpine.sophus.math.SimplexD;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.red.Mean;

class PlatonicSolidTest {
  @ParameterizedTest
  @EnumSource
  void testStructure(PlatonicSolid platonicSolid) {
    List<int[]> faces = platonicSolid.faces();
    assertEquals(faces.size(), platonicSolid.faceCount());
    assertEquals(faces.stream().map(a -> a.length).distinct().toList(), List.of(platonicSolid.faceShape()));
  }

  @ParameterizedTest
  @EnumSource
  void testCenter(PlatonicSolid platonicSolid) {
    Tolerance.CHOP.requireAllZero(Mean.of(platonicSolid.vertices()));
  }

  @ParameterizedTest
  @EnumSource
  void testBorder(PlatonicSolid platonicSolid) {
    Set<Tensor> border = SimplexD.of(platonicSolid.faces());
    assertTrue(border.isEmpty());
  }
}
