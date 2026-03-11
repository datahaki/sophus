// code by jph
package ch.alpine.sophus.lie;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ch.alpine.sophus.SophusExperimental;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;

class SpecificLieGroupTest {
  static List<SpecificLieGroup> specificLieGroup() {
    return SophusExperimental.filter(SpecificLieGroup.class);
  }

  @ParameterizedTest
  @MethodSource("specificLieGroup")
  void testSimple(SpecificLieGroup specificLieGroup) {
    Tensor ad = MatrixAlgebra.of(specificLieGroup).ad();
    int n = ad.length();
    Integers.requirePositive(n);
    // Tensor p = RandomSample.of(specificLieGroup);
    // Tensor q = RandomSample.of(specificLieGroup);
    // LieExponential lieExponential = specificLieGroup.exponential0();
    // Tensor x = lieExponential.log(p);
    // Tensor y = lieExponential.log(q);
    // Tensor x = RandomVariate.of(NormalDistribution.of(0, 0.2), n);
    // Tensor y = RandomVariate.of(NormalDistribution.of(0, 0.2), n);
    // Tensor z = ad.dot(x).dot(y);
    // Tensor glX = lieExponential.gl_representation(x);
    // Tensor glY = lieExponential.gl_representation(y);
    // Tensor glZ = lieExponential.gl_representation(z);
    // Tensor glXY = MatrixBracket.of(glX, glY);
    // Tolerance.CHOP.requireClose(glZ, glXY);
  }
}
