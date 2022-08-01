// code by jph
package ch.alpine.sophus.hs.r3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

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
  }
}
