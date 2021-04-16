// code by jph
package ch.ethz.idsc.sophus.hs.st;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.nrm.Matrix2Norm;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class StExponentialTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    for (int n = 3; n < 6; ++n)
      for (int k = n - 2; k <= n; ++k) {
        RandomSampleInterface randomSampleInterface = StRandomSample.of(n, k);
        Tensor p = RandomSample.of(randomSampleInterface);
        StMemberQ.INSTANCE.require(p);
        TStProjection tStProjection = new TStProjection(p);
        Tensor v = tStProjection.apply(RandomVariate.of(NormalDistribution.standard(), k, n));
        assertTrue(Scalars.lessThan(RealScalar.of(0.01), Matrix2Norm.of(v)));
        StExponential stExponential = Serialization.copy(new StExponential(p));
        Tensor q = stExponential.exp(v);
        assertEquals(Dimensions.of(p), Dimensions.of(q));
        StMemberQ.INSTANCE.require(q);
      }
  }
}
