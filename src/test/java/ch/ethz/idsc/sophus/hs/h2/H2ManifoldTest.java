// code by jph
package ch.ethz.idsc.sophus.hs.h2;

import ch.ethz.idsc.sophus.hs.hn.HnManifold;
import ch.ethz.idsc.sophus.hs.hn.HnMetric;
import ch.ethz.idsc.sophus.hs.hn.HnNorm;
import ch.ethz.idsc.sophus.hs.hn.HnWeierstrassCoordinate;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Hypot;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class H2ManifoldTest extends TestCase {
  public void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 2));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 2));
      VectorQ.requireLength(p, 3);
      Scalar distance = HnMetric.INSTANCE.distance(p, q);
      Tensor log_h2 = H2Manifold.INSTANCE.logAt(p).vectorLog(q);
      VectorQ.requireLength(log_h2, 2);
      Chop._08.requireClose(Hypot.ofVector(log_h2), distance);
      Tensor log_hn = HnManifold.INSTANCE.logAt(p).vectorLog(q);
      Scalar norm = HnNorm.INSTANCE.norm(log_hn);
      Chop._08.requireClose(distance, norm);
    }
  }
}
