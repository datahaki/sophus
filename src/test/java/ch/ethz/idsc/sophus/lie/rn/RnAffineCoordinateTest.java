// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.math.win.AffineCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class RnAffineCoordinateTest extends TestCase {
  public void testSimple() {
    for (int dim = 2; dim < 4; ++dim)
      for (int length = dim + 1; length < 10; ++length) {
        Distribution distribution = NormalDistribution.standard();
        Tensor sequence = RandomVariate.of(distribution, length, dim);
        Tensor mean = RandomVariate.of(distribution, dim);
        Tensor lhs = RnAffineCoordinate.INSTANCE.weights(sequence, mean);
        Tensor rhs = AffineCoordinate.INSTANCE.weights(sequence, mean);
        Chop._10.requireClose(lhs, rhs);
      }
  }
}
