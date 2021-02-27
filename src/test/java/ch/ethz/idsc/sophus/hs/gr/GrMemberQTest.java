// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.util.Arrays;

import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.lie.TensorProduct;
import junit.framework.TestCase;

public class GrMemberQTest extends TestCase {
  public void testSimple() {
    int n = 5;
    Tensor x = RandomSample.of(GrRandomSample.of(n, 3));
    assertEquals(Dimensions.of(x), Arrays.asList(n, n));
    GrMemberQ.INSTANCE.require(x);
  }

  public void testVectorProject() {
    for (int n = 1; n < 6; ++n) {
      Tensor normal = RandomSample.of(SnRandomSample.of(n));
      Tensor x = TensorProduct.of(normal, normal);
      GrMemberQ.INSTANCE.require(x);
    }
  }

  public void testNullFail() {
    AssertFail.of(() -> GrMemberQ.INSTANCE.test(null));
  }
}
