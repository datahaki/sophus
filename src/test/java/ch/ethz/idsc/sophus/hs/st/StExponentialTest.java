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

public class StExponentialTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    int k = 3;
    int n = 5;
    RandomSampleInterface randomSampleInterface = StRandomSample.of(n, k);
    Tensor x = RandomSample.of(randomSampleInterface);
    StMemberQ.INSTANCE.require(x);
    TStProjection tStProjection = new TStProjection(x);
    Tensor v = tStProjection.apply(RandomVariate.of(NormalDistribution.standard(), k, n));
    assertTrue(Scalars.lessThan(RealScalar.of(0.01), Matrix2Norm.of(v)));
    StExponential stExponential = Serialization.copy(new StExponential(x));
    // stExponential.exp(v);
  }
}
