// code by jph
package ch.alpine.sophus.math.sample;

import java.io.Serializable;
import java.util.random.RandomGenerator;

import ch.alpine.sophus.hs.sn.SnRandomSample;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.pow.Power;

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

  // ---
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
  public Tensor randomSample(RandomGenerator randomGenerator) {
    Tensor vector = randomSampleInterface.randomSample(randomGenerator).multiply(power.apply(RandomVariate.of(UNIFORM, randomGenerator)));
    return vector.multiply(radius).add(center);
  }
}
