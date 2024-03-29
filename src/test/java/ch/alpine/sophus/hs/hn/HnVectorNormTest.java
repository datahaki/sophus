// code by jph
package ch.alpine.sophus.hs.hn;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class HnVectorNormTest {
  @Test
  void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 2; d < 5; ++d) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      VectorQ.requireLength(p, d + 1);
      Scalar distance = HnManifold.INSTANCE.distance(p, q); // uses HnAngle
      Tensor log_hn = HnManifold.INSTANCE.exponential(p).log(q);
      VectorQ.requireLength(log_hn, d + 1);
      Scalar norm = HnVectorNorm.of(log_hn);
      Chop._08.requireClose(distance, norm);
      Tensor r = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      TensorUnaryOperator tuo = HnManifold.INSTANCE.hsTransport().shift(p, r);
      Tensor tv = tuo.apply(log_hn);
      assertFalse(new THnMemberQ(p).test(tv));
      new THnMemberQ(r).require(tv);
      Chop._08.requireClose(distance, HnVectorNorm.of(tv));
    }
  }

  @Test
  void testNormalizeExp() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor xn = RandomVariate.of(distribution, d);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, RandomVariate.of(distribution, d));
      v = HnVectorNorm.NORMALIZE.apply(v);
      Tolerance.CHOP.requireClose(HnVectorNorm.of(v), RealScalar.ONE);
    }
  }
}
