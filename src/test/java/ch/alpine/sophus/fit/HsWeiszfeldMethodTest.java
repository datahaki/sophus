// code by jph
package ch.alpine.sophus.fit;

import ch.alpine.sophus.hs.MetricBiinvariant;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class HsWeiszfeldMethodTest extends TestCase {
  public void testSimple() {
    SpatialMedian sm2 = WeiszfeldMethod.with(Tolerance.CHOP);
    Distribution distribution = NormalDistribution.standard();
    int fails = 0;
    for (int d = 2; d < 5; ++d)
      for (int n = 2; n < 20; n += 4)
        try {
          Tensor sequence = RandomVariate.of(distribution, n, d);
          TensorUnaryOperator create = MetricBiinvariant.EUCLIDEAN.weighting(RnManifold.INSTANCE, InversePowerVariogram.of(1), sequence);
          SpatialMedian sm1 = HsWeiszfeldMethod.of( //
              RnBiinvariantMean.INSTANCE, //
              create, //
              Tolerance.CHOP);
          Tensor p1 = sm1.uniform(sequence).get();
          Tensor p2 = sm2.uniform(sequence).get();
          Chop._07.requireClose(p1, p2);
        } catch (Exception exception) {
          ++fails;
        }
    assertTrue(fails < 4);
  }
}
