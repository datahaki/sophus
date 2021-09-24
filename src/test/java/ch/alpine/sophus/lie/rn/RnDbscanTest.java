// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.math.sample.BoxRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.opt.nd.NdBox;
import ch.alpine.tensor.opt.nd.NdCenterBase;
import junit.framework.TestCase;

public class RnDbscanTest extends TestCase {
  public void testSimple() {
    
    RnDbscan rnDbscan = new RnDbscan(RealScalar.of(0.1), 3);
    RandomSampleInterface randomSampleInterface = BoxRandomSample.of(NdBox.of(Tensors.vector(0, 0), Tensors.vector(1, 1)));
    Tensor points = RandomSample.of(randomSampleInterface, 40);
    Integer[] integers = rnDbscan.cluster(points, NdCenterBase::of2Norm);
    assertEquals(integers.length, 40);
  }
}
