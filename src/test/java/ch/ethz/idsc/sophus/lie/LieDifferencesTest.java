// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.IOException;
import java.util.Arrays;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.se2.Se2Differences;
import ch.ethz.idsc.sophus.lie.se2.Se2RandomSample;
import ch.ethz.idsc.sophus.lie.se3.Se3Differences;
import ch.ethz.idsc.sophus.lie.se3.Se3Matrix;
import ch.ethz.idsc.sophus.lie.so3.Rodrigues;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Differences;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class LieDifferencesTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.unit();
    Tensor tensor = RandomVariate.of(distribution, 10, 4);
    LieDifferences lieDifferences = //
        Serialization.copy(new LieDifferences(RnManifold.INSTANCE));
    assertEquals(lieDifferences.apply(tensor), Differences.of(tensor));
  }

  public void testPairFunction() {
    Distribution distribution = UniformDistribution.unit();
    RandomSampleInterface randomSampleInterface = Se2RandomSample.of(distribution);
    LieDifferences lieDifferences = Se2Differences.INSTANCE;
    for (int index = 0; index < 10; ++index) {
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      Tensor v1 = lieDifferences.pair(p, q);
      Tensor v2 = lieDifferences.pair(q, p).negate();
      Chop._12.requireClose(v1, v2);
    }
  }

  public void testSe3() {
    Distribution distribution = NormalDistribution.of(0, .1);
    Tensor tensor = Tensors.empty();
    for (int index = 0; index < 10; ++index)
      tensor.append(Se3Matrix.of( //
          Rodrigues.vectorExp(RandomVariate.of(distribution, 3)), RandomVariate.of(distribution, 3)));
    LieDifferences lieDifferences = Se3Differences.INSTANCE;
    assertEquals(Dimensions.of(lieDifferences.apply(tensor)), Arrays.asList(9, 2, 3));
  }

  public void testLieGroupNullFail() {
    AssertFail.of(() -> new LieDifferences(null));
  }
}
