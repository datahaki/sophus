// code by jph
package ch.alpine.sophus.lie.so;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.pd.Orthogonalize;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class SoProjectionTest {
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  void testProject(int n) {
    Tensor x = RandomVariate.of(NormalDistribution.standard(), n, n);
    Tensor matrix = SoProjection.project(x);
    Scalar scalar = Det.of(matrix);
    Chop._10.requireClose(scalar, RealScalar.ONE);
    Tensor polard = Orthogonalize.usingPD(matrix); // does not work with x
    Chop._10.requireClose(matrix, polard);
  }
}
