// code by jph
package ch.alpine.sophus.hs.st;

import java.io.IOException;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Matrix2Norm;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class TStProjectionTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Random random = new Random(7);
    for (int n = 3; n < 6; ++n)
      for (int k = n - 2; k <= n; ++k) {
        RandomSampleInterface randomSampleInterface = StRandomSample.of(n, k);
        Tensor x = RandomSample.of(randomSampleInterface, random);
        StMemberQ.INSTANCE.require(x);
        TStProjection tStProjection = Serialization.copy(new TStProjection(x));
        Tensor c = RandomVariate.of(NormalDistribution.standard(), random, k, n);
        Tensor v = tStProjection.apply(c);
        assertTrue(Scalars.lessThan(RealScalar.of(0.01), Matrix2Norm.of(v)));
        assertTrue(Serialization.copy(new TStMemberQ(x)).test(v));
        Tensor v2 = tStProjection.apply(v);
        Tolerance.CHOP.requireClose(v, v2);
      }
  }
}
