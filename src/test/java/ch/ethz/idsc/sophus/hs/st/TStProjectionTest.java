// code by jph
package ch.ethz.idsc.sophus.hs.st;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.nrm.Matrix2Norm;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class TStProjectionTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    int k = 3;
    int n = 5;
    RandomSampleInterface randomSampleInterface = StRandomSample.of(n, k);
    Tensor x = RandomSample.of(randomSampleInterface);
    StMemberQ.INSTANCE.require(x);
    TStProjection tStProjection = Serialization.copy(new TStProjection(x));
    Tensor c = RandomVariate.of(NormalDistribution.standard(), k, n);
    Tensor v = tStProjection.apply(c);
    assertTrue(Scalars.lessThan(RealScalar.of(0.01), Matrix2Norm.of(v)));
    assertTrue(Serialization.copy(new TStMemberQ(x)).test(v));
  }
}
