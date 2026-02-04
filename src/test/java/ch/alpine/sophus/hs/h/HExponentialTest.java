package ch.alpine.sophus.hs.h;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;

class HExponentialTest {
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 5 })
  void testZero(int d) {
    Tensor px = RandomVariate.of(NormalDistribution.standard(), d);
    HExponential hExponential = new HExponential(px);
    Tensor exp = hExponential.exp(Array.zeros(d));
    Tolerance.CHOP.requireClose(px, exp);
  }

  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 5 })
  void testPush(int d) {
    Tensor px = RandomVariate.of(NormalDistribution.standard(), d);
    HExponential hExponential = new HExponential(px);
    Tensor vx = RandomVariate.of(NormalDistribution.standard(), d);
    Tensor exp = hExponential.exp(vx);
    Tensor log = hExponential.log(exp);
    Chop._10.requireClose(vx, log);
    Scalar dist = Hyperboloid.INSTANCE.distance(px, exp);
    Sign.requirePositiveOrZero(dist);
  }
}
