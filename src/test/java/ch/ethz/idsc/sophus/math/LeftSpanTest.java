// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dot;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class LeftSpanTest extends TestCase {
  public void testRightKernel() {
    Tensor matrix = RandomVariate.of(NormalDistribution.standard(), 3, 10);
    Tensor vector = RandomVariate.of(NormalDistribution.standard(), 10);
    Tensor kernel = LeftSpan.kernel(vector, Transpose.of(matrix));
    Chop._12.requireAllZero(matrix.dot(kernel));
  }

  public void testRightImage() {
    Tensor matrix = RandomVariate.of(NormalDistribution.standard(), 3, 10);
    Tensor vector = RandomVariate.of(NormalDistribution.standard(), 10);
    Tensor vimage = LeftSpan.image(vector, Transpose.of(matrix));
    Chop._10.requireClose( //
        matrix.dot(vector), //
        matrix.dot(vimage));
  }

  public void testLeftKernel() {
    Tensor matrix = RandomVariate.of(NormalDistribution.standard(), 10, 3);
    Tensor vector = RandomVariate.of(NormalDistribution.standard(), 10);
    Tensor kernel = LeftSpan.kernel(vector, matrix);
    Chop._12.requireAllZero(kernel.dot(matrix));
  }

  public void testLeftImage() {
    Tensor matrix = RandomVariate.of(NormalDistribution.standard(), 10, 3);
    Tensor vector = RandomVariate.of(NormalDistribution.standard(), 10);
    Tensor vimage = LeftSpan.image(vector, matrix);
    Chop._10.requireClose( //
        vector.dot(matrix), //
        vimage.dot(matrix));
  }

  private static Tensor deprec(Tensor vector, Tensor nullsp) {
    return vector.dot(PseudoInverse.of(nullsp)).dot(nullsp);
  }

  public void testSimple() {
    Distribution distribution = UniformDistribution.unit();
    for (int count = 0; count < 10; ++count) {
      Tensor vector = RandomVariate.of(distribution, 10);
      Tensor matrix = RandomVariate.of(distribution, 10, 3);
      Tensor nullsp = LeftNullSpace.usingQR(matrix);
      Tensor p1 = deprec(vector, nullsp);
      Tensor p2 = Dot.of(nullsp, vector, nullsp);
      Tolerance.CHOP.requireClose(p1, p2);
    }
  }
}
