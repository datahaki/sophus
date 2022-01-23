// code by jph
package ch.alpine.sophus.bm;

import java.util.Random;

import ch.alpine.sophus.hs.sn.SnBiinvariantMean;
import ch.alpine.sophus.hs.sn.SnExponential;
import ch.alpine.sophus.lie.HsAlgebra;
import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.so3.So3Algebra;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class HsBiinvariantMeanTest extends TestCase {
  public void testMean() {
    Distribution distributionX = UniformDistribution.of(-0.05, 0.05);
    Distribution distributionW = UniformDistribution.of(0.2, 1);
    LieAlgebra lieAlgebra = So3Algebra.INSTANCE;
    HsAlgebra hsAlgebra = new HsAlgebra(lieAlgebra.ad(), 2, 8);
    Random random = new Random(1);
    for (int n = 3; n < 7; ++n) {
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distributionW, random, n));
      Tensor sequence_m = RandomVariate.of(distributionX, random, n, 2);
      BiinvariantMean biinvariantMean = HsBiinvariantMean.of(hsAlgebra, Tolerance.CHOP);
      Tensor m_avg = biinvariantMean.mean(sequence_m, weights);
      SnExponential snExponential = new SnExponential(UnitVector.of(3, 2));
      Tensor pointsS2 = Tensor.of(sequence_m.stream().map(r -> r.copy().append(RealScalar.ZERO)).map(snExponential::exp));
      Tensor meanS2 = SnBiinvariantMean.INSTANCE.mean(pointsS2, weights);
      Tensor res = snExponential.log(meanS2).extract(0, 2);
      Chop._11.requireClose(m_avg, res);
    }
  }
}
