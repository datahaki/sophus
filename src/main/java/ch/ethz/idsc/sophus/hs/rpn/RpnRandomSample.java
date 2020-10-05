// code by jph
package ch.ethz.idsc.sophus.hs.rpn;

import java.io.Serializable;
import java.util.Random;

import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Integers;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Abs;

public class RpnRandomSample implements RandomSampleInterface, Serializable {
  private static final long serialVersionUID = 3982233339122358560L;

  /** Example:
   * dimension == 1 corresponds to the half circle in the plane
   * 
   * @param dimension of manifold
   * @return */
  public static RandomSampleInterface of(int dimension) {
    return new RpnRandomSample(Integers.requirePositiveOrZero(dimension));
  }

  /***************************************************/
  private final RandomSampleInterface randomSampleInterface;

  private RpnRandomSample(int dimension) {
    randomSampleInterface = SnRandomSample.of(dimension);
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    Tensor tensor = RandomSample.of(randomSampleInterface);
    tensor.set(Abs.FUNCTION, 0);
    return tensor;
  }
}
