package ch.alpine.sophus.lie;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ch.alpine.sophus.SophusExperimental;

class LieAlgebraMatrixBasisTest {
  static List<SpecificLieGroup> specificLieGroup() {
    return SophusExperimental.filter(SpecificLieGroup.class);
  }

  @ParameterizedTest
  @MethodSource("specificLieGroup")
  void testSimple(SpecificLieGroup specificLieGroup) {
    MatrixAlgebra.of(specificLieGroup).ad();
  }
}
