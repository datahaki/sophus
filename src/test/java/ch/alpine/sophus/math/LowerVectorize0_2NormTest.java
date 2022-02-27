// code by jph
package ch.alpine.sophus.math;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import junit.framework.TestCase;

public class LowerVectorize0_2NormTest extends TestCase {
  public void testSimple() {
    for (int n = 1; n < 10; ++n) {
      Tensor matrix = Symmetrize.of(RandomVariate.of(UniformDistribution.unit(), n, n));
      Scalar n1 = FrobeniusNorm.of(matrix);
      Scalar n2 = LowerVectorize0_2Norm.INSTANCE.norm(LowerVectorize.of(matrix, 0));
      Tolerance.CHOP.requireClose(n1, n2);
    }
  }

  public void testLengthFail() {
    AssertFail.of(() -> LowerVectorize0_2Norm.INSTANCE.norm(Pi.VALUE));
    AssertFail.of(() -> LowerVectorize0_2Norm.INSTANCE.norm(Tensors.vector()));
    AssertFail.of(() -> LowerVectorize0_2Norm.INSTANCE.norm(Tensors.vector(1, 2)));
    AssertFail.of(() -> LowerVectorize0_2Norm.INSTANCE.norm(Tensors.vector(1, 2, 3, 4)));
  }

  public void testRequireTriangleNumber() {
    LowerVectorize0_2Norm.requireTriangleNumber(1);
    LowerVectorize0_2Norm.requireTriangleNumber(3);
    LowerVectorize0_2Norm.requireTriangleNumber(6);
    LowerVectorize0_2Norm.requireTriangleNumber(10);
    AssertFail.of(() -> LowerVectorize0_2Norm.requireTriangleNumber(2));
    AssertFail.of(() -> LowerVectorize0_2Norm.requireTriangleNumber(4));
  }
}
