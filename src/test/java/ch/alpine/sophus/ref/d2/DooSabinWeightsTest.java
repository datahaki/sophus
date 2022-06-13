// code by jph
package ch.alpine.sophus.ref.d2;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Chop;

class DooSabinWeightsTest {
  @Test
  void testSimple() {
    for (int n = 3; n <= 12; ++n) {
      Tensor w = DooSabinWeights.INSTANCE.apply(n);
      AffineQ.require(w, Chop._08);
      Chop._12.requireClose(w, DooSabinWeights.numeric(n));
    }
  }
}
