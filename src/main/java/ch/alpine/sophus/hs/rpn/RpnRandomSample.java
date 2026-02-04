// code by jph
package ch.alpine.sophus.hs.rpn;

import java.io.Serializable;
import java.util.random.RandomGenerator;

import ch.alpine.sophus.hs.s.Sphere;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.sca.Abs;

// TODO SOPHUS this is redundant to Hemisphere!!!
public class RpnRandomSample implements RandomSampleInterface, Serializable {
  /** Example:
   * dimension == 1 corresponds to the half circle in the plane
   * 
   * @param dimension of manifold
   * @return */
  public static RandomSampleInterface of(int dimension) {
    return new RpnRandomSample(Integers.requirePositiveOrZero(dimension));
  }

  // ---
  private final RandomSampleInterface randomSampleInterface;

  private RpnRandomSample(int dimension) {
    randomSampleInterface = new Sphere(dimension);
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    Tensor tensor = randomSampleInterface.randomSample(randomGenerator);
    tensor.set(Abs.FUNCTION, tensor.length() - 1);
    return tensor;
  }
}
