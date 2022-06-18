// code by jph
package ch.alpine.sophus.fit;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.dv.Biinvariant;
import ch.alpine.sophus.dv.Biinvariants;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class HsWeiszfeldMethodTest {
  @Test
  void testSimple() {
    Random random = new Random(3);
    SpatialMedian sm2 = new WeiszfeldMethod(Tolerance.CHOP);
    Distribution distribution = NormalDistribution.standard();
    Biinvariant biinvariant = Biinvariants.METRIC.of(RnGroup.INSTANCE);
    for (int d = 2; d < 5; ++d)
      for (int n = 2; n < 20; n += 4) {
        Tensor sequence = RandomVariate.of(distribution, random, n, d);
        Sedarim create = biinvariant.weighting(InversePowerVariogram.of(1), sequence);
        SpatialMedian sm1 = new HsWeiszfeldMethod(RnBiinvariantMean.INSTANCE, create, Tolerance.CHOP);
        Tensor p1 = sm1.uniform(sequence).get();
        Tensor p2 = sm2.uniform(sequence).get();
        Chop._07.requireClose(p1, p2);
      }
  }
}
