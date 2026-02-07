// code by jph
package ch.alpine.sophus.lie.sl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Trace;

class SlGroupTest {
  @Test
  void testString() {
    assertTrue(SlGroup.INSTANCE.toString().startsWith("SL"));
  }

  @RepeatedTest(10)
  void testTraceZero(RepetitionInfo repetitionInfo) {
    int n = repetitionInfo.getCurrentRepetition();
    Tensor matrix = RandomVariate.of(UniformDistribution.unit(20), n, n);
    Scalar scalar = Trace.of(matrix);
    Tensor diff = matrix.subtract(IdentityMatrix.of(n).multiply(scalar.divide(RealScalar.of(n))));
    Tolerance.CHOP.requireZero(Trace.of(diff));
    Tensor result = MatrixExp.of(diff);
    Tolerance.CHOP.requireClose(Det.of(result), RealScalar.ONE);
  }
}
