// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.IOException;
import java.util.Arrays;

import ch.ethz.idsc.sophus.lie.gl.LinearGroup;
import ch.ethz.idsc.sophus.lie.rn.RnExponential;
import ch.ethz.idsc.sophus.lie.rn.RnGroup;
import ch.ethz.idsc.sophus.lie.se2.Se2Group;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringExponential;
import ch.ethz.idsc.sophus.lie.se3.Se3Exponential;
import ch.ethz.idsc.sophus.lie.se3.Se3Matrix;
import ch.ethz.idsc.sophus.lie.so3.So3Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Differences;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.io.Serialization;
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
        Serialization.copy(new LieDifferences(RnGroup.INSTANCE, RnExponential.INSTANCE));
    assertEquals(lieDifferences.apply(tensor), Differences.of(tensor));
  }

  public void testSe2() {
    Distribution distribution = UniformDistribution.unit();
    Tensor tensor = RandomVariate.of(distribution, 10, 3);
    LieDifferences lieDifferences = //
        new LieDifferences(Se2Group.INSTANCE, Se2CoveringExponential.INSTANCE);
    assertEquals(Dimensions.of(lieDifferences.apply(tensor)), Arrays.asList(9, 3));
  }

  public void testSe2antiCommute() {
    Distribution distribution = UniformDistribution.unit();
    LieDifferences lieDifferences = //
        new LieDifferences(Se2Group.INSTANCE, Se2CoveringExponential.INSTANCE);
    for (int index = 0; index < 10; ++index) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
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
          So3Exponential.INSTANCE.exp(RandomVariate.of(distribution, 3)), RandomVariate.of(distribution, 3)));
    LieDifferences lieDifferences = //
        new LieDifferences(LinearGroup.INSTANCE, Se3Exponential.INSTANCE);
    assertEquals(Dimensions.of(lieDifferences.apply(tensor)), Arrays.asList(9, 2, 3));
  }

  public void testLieGroupNullFail() {
    try {
      new LieDifferences(null, Se2CoveringExponential.INSTANCE);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testLieExponentialNullFail() {
    try {
      new LieDifferences(Se2Group.INSTANCE, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
