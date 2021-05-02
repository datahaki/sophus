// code by jph
package ch.alpine.sophus.lie.se2;

import java.util.Arrays;
import java.util.Random;

import ch.alpine.sophus.hs.Biinvariants;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.pdf.LogNormalDistribution;
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