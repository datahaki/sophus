// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class HnNormTest extends TestCase {
  public void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 2; d < 5; ++d) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      VectorQ.requireLength(p, d + 1);
      Scalar distance = HnMetric.INSTANCE.distance(p, q); // uses HnAngle
      Tensor log_hn = HnManifold.INSTANCE.exponential(p).log(q);
      VectorQ.requireLength(log_hn, d + 1);
      Scalar norm = HnVectorNorm.of(log_hn);
      Chop._08.requireClose(distance, norm);
      Tensor r = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      TensorUnaryOperator tuo = HnTransport.INSTANCE.shift(p, r);
      Tensor tv = tuo.apply(log_hn);
      assertFalse(new THnMemberQ(p).test(tv));
      new THnMemberQ(r).require(tv);
      Chop._08.requireClose(distance, HnVectorNorm.of(tv));
    }
  }
}
