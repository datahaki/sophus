// code by jph
package ch.alpine.sophus.lie;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ch.alpine.sophus.SophusExperimental;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

class SpecificLieGroupTest {
  static List<SpecificLieGroup> specificLieGroup() {
    return SophusExperimental.filter(SpecificLieGroup.class);
  }

  @ParameterizedTest
  @MethodSource("specificLieGroup")
  void testSimple(SpecificLieGroup specificLieGroup) {
    MatrixAlgebra matrixAlgebra = MatrixAlgebra.of(specificLieGroup);
    Tensor ad = matrixAlgebra.ad();
    int n = ad.length();
    Integers.requirePositive(n);
    Tensor p = RandomSample.of(specificLieGroup);
    Tensor neutral = specificLieGroup.neutral(p);
    specificLieGroup.isPointQ().require(neutral);
    specificLieGroup.isPointQ().require(p);
    LieExponential lieExponential = specificLieGroup.lieExponential();
    // ---
    Tensor log = lieExponential.log(neutral);
    Tolerance.CHOP.requireAllZero(log);
    ZeroDefectArrayQ zeroDefectArrayQ = lieExponential.isTangentQ();
    List<Integer> dims = Dimensions.of(log);
    LinearSubspace linearSubspace = LinearSubspace.of(zeroDefectArrayQ::defect, dims);
    Tensor x = linearSubspace.projection(RandomVariate.of(NormalDistribution.of(0.0, 0.1), dims));
    Tensor y = linearSubspace.projection(RandomVariate.of(NormalDistribution.of(0.0, 0.1), dims));
    Tensor glX = lieExponential.gl_representation(x);
    Tensor glY = lieExponential.gl_representation(y);
    Tensor glXY = MatrixBracket.of(glX, glY);
    glXY.maps(Scalar::zero);
    // IO.println(specificLieGroup);
    // IO.println(Pretty.of(glXY.maps(Round._6)));
    // Tolerance.CHOP.requireClose(glZ, glXY);
  }
}
