// code by jph
package ch.alpine.sophus.hs.st;

import java.io.IOException;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.nrm.Matrix2Norm;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class StExponentialTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Random random = new Random(4);
    for (int n = 3; n < 6; ++n)
      for (int k = n - 2; k <= n; ++k) {
        RandomSampleInterface randomSampleInterface = StRandomSample.of(n, k);
        Tensor p = RandomSample.of(randomSampleInterface, random);
        StMemberQ.INSTANCE.require(p);
        TStProjection tStProjection = new TStProjection(p);
        Tensor v = tStProjection.apply(RandomVariate.of(NormalDistribution.standard(), random, k, n));
        assertTrue(Scalars.lessThan(RealScalar.of(0.01), Matrix2Norm.of(v)));
        StExponential stExponential = Serialization.copy(new StExponential(p));
        Tensor q = stExponential.exp(v);
        assertEquals(Dimensions.of(p), Dimensions.of(q));
        StMemberQ.INSTANCE.require(q);
      }
  }
}
