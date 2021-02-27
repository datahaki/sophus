// code by jph
package ch.ethz.idsc.sophus.lie.se2;

import java.util.Arrays;
import java.util.Random;

import ch.ethz.idsc.sophus.hs.Biinvariants;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.pdf.LogNormalDistribution;
import junit.framework.TestCase;

public class Se2ManifoldTest extends TestCase {
  public void testSimple() {
    int n = 5 + new Random().nextInt(5);
    Tensor sequence = RandomSample.of(Se2RandomSample.of(LogNormalDistribution.standard()), n);
    TensorUnaryOperator tuo = Biinvariants.HARBOR.distances(Se2Manifold.INSTANCE, sequence);
    Tensor matrix = Tensor.of(sequence.stream().map(tuo));
    assertEquals(Dimensions.of(matrix), Arrays.asList(n, n));
    assertTrue(SymmetricMatrixQ.of(matrix));
    // matrix entry i,j contains frobenius norm between
    // projection matrices at point i, and at point j
  }
}
