// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpanProjectionTest extends TestCase {
  public void testKernel() {
    Tensor matrix = RandomVariate.of(NormalDistribution.standard(), 3, 10);
    Tensor vector = RandomVariate.of(NormalDistribution.standard(), 10);
    Tensor kernel = SpanProjection.kernel(Transpose.of(matrix), vector);
    Chop._12.requireAllZero(matrix.dot(kernel));
  }
}
