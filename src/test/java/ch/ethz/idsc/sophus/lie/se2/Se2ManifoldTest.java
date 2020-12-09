// code by jph
package ch.ethz.idsc.sophus.lie.se2;

import java.util.Arrays;

import ch.ethz.idsc.sophus.hs.Biinvariants;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class Se2ManifoldTest extends TestCase {
  public void testSimple() {
    Tensor sequence = RandomVariate.of(NormalDistribution.standard(), 10, 3);
    TensorUnaryOperator tuo = Biinvariants.HARBOR.distances(Se2Manifold.INSTANCE, sequence);
    Tensor matrix = Tensor.of(sequence.stream().map(tuo));
    assertEquals(Dimensions.of(matrix), Arrays.asList(10, 10));
    assertTrue(SymmetricMatrixQ.of(matrix));
  }
}
