// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class ScBiinvariantMeanTest extends TestCase {
  public void testSimple() {
    Tensor scalar = ScBiinvariantMean.INSTANCE.mean(Tensors.vector(1, 2, 3).map(Tensors::of), Tensors.fromString("{1/3, 1/3, 1/3}"));
    Chop._10.requireClose(scalar, Tensors.vector(1.8171205928321397));
  }
}
