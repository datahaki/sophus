// code by jph
package ch.alpine.sophus.hs.sn;

import java.util.Random;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.hs.HsAlgebra;
import ch.alpine.sophus.hs.HsBiinvariantMean;
import ch.alpine.sophus.math.sample.BallRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Insert;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import junit.framework.TestCase;

public class SnAlgebraTest extends TestCase {
  public void testProperty() {
    for (int d = 2; d < 5; ++d) {
      HsAlgebra hsAlgebra = SnAlgebra.of(d);
      assertFalse(hsAlgebra.isHTrivial());
      assertTrue(hsAlgebra.isReductive());
      assertTrue(hsAlgebra.isSymmetric());
    }
  }

  public void testBiinvariantMean() {
    Random random = new Random(3);
    Distribution distribution = UniformDistribution.of(-0.2, 1);
    for (int d = 2; d < 5; ++d) {
      HsAlgebra hsAlgebra = SnAlgebra.of(d);
      RandomSampleInterface randomSampleInterface = BallRandomSample.of(Array.zeros(d), RealScalar.of(0.05));
      for (int n = d + 0; n < d + 4; ++n) {
        Tensor sequence = RandomSample.of(randomSampleInterface, random, n);
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, random, n));
        BiinvariantMean biinvariantMean = HsBiinvariantMean.of(hsAlgebra);
        final Tensor hsmean = biinvariantMean.mean(sequence, weights);
        SnExponential snExponential = new SnExponential(UnitVector.of(d + 1, 0));
        Tensor sn_pnt = Tensor.of(sequence.stream().map(p -> snExponential.exp(Insert.of(p, RealScalar.ZERO, 0))));
        Tensor snmean = SnBiinvariantMean.INSTANCE.mean(sn_pnt, weights);
        final Tensor sn_cmp = snExponential.log(snmean).extract(1, d + 1);
        Tolerance.CHOP.requireClose(hsmean, sn_cmp);
      }
    }
  }
}
