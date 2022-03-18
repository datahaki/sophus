// code by jph
package ch.alpine.sophus.hs.gr;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.sn.SnRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.lie.TensorProduct;

public class GrMemberQTest {
  @Test
  public void testSimple() {
    int n = 5;
    Tensor x = RandomSample.of(new GrRandomSample(n, 3));
    assertEquals(Dimensions.of(x), Arrays.asList(n, n));
    GrMemberQ.INSTANCE.require(x);
  }

  @Test
  public void testVectorProject() {
    for (int n = 1; n < 6; ++n) {
      Tensor normal = RandomSample.of(SnRandomSample.of(n));
      Tensor x = TensorProduct.of(normal, normal);
      GrMemberQ.INSTANCE.require(x);
    }
  }

  @Test
  public void testNullFail() {
    AssertFail.of(() -> GrMemberQ.INSTANCE.test(null));
  }
}
