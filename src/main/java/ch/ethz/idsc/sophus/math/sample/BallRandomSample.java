// code by jph
package ch.ethz.idsc.sophus.math.sample;

import java.io.Serializable;
import java.util.Random;

import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Power;
import ch.ethz.idsc.tensor.sca.Sign;

/** uniform random samples from the interior of a n-dimensional sphere
 * 
 * implementation supports the use of Quantity
 * 
 * <p>Reference:
 * "Spheres and Rotations" in NR, 2007
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Ball.html">Ball</a> */
public class BallRandomSample implements RandomSampleInterface, Serializable {
  private static final long serialVersionUID = -1164346871380552006L;
  private static final Distribution UNIFORM = UniformDistribution.unit();

  /** @param center non-empty vector of length less equals to 10
   * @param radius non-negative
   * @return
   * @throws Exception if center is not a vector
   * @throws Exception if radius is negative */
  public static RandomSampleInterface of(Tensor center, Scalar radius) {
    return new BallRandomSample( //
        VectorQ.require(center), //
        Sign.requirePositiveOrZero(radius));
  }

  /***************************************************/
  private final Tensor center;
  private final Scalar radius;
  private final RandomSampleInterface randomSampleInterface;
  private final ScalarUnaryOperator power;

  private BallRandomSample(Tensor center, Scalar radius) {
    randomSampleInterface = SnRandomSample.of(center.length() - 1);
    power = Power.function(RationalScalar.of(1, center.length()));
    this.center = center;
    this.radius = radius;
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    Tensor vector = randomSampleInterface.randomSample(random).multiply(power.apply(RandomVariate.of(UNIFORM, random)));
    return vector.multiply(radius).add(center);
  }
}
