// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.sophus.krg.InversePowerVariogram;
import ch.ethz.idsc.sophus.krg.PseudoDistances;
import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.SpatialMedian;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.opt.WeiszfeldMethod;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
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
          TensorUnaryOperator create = PseudoDistances.ABSOLUTE.normalized(RnManifold.INSTANCE, InversePowerVariogram.of(1), sequence);
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
