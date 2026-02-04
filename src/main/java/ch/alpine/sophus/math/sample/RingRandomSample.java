// code by jph
package ch.alpine.sophus.math.sample;

import java.io.Serializable;
import java.util.random.RandomGenerator;

import ch.alpine.sophus.hs.s.Sphere;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.pow.Power;

/** uniform random samples from the interior of an n-dimensional sphere
 * 
 * implementation supports the use of Quantity
 * 
 * <p>Reference:
 * "Spheres and Rotations" in NR, 2007
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Ball.html">Ball</a> */
public class RingRandomSample implements RandomSampleInterface, Serializable {
  private final Scalar r2;
  private final RandomSampleInterface randomSampleInterface;
  private final ScalarUnaryOperator power;
  private final Distribution distribution;

  public RingRandomSample(int dimensions, Scalar r1, Scalar r2) {
    randomSampleInterface = new Sphere(dimensions - 1);
    power = Power.function(RationalScalar.of(1, dimensions));
    this.r2 = r2;
    distribution = UniformDistribution.of(Power.of(r1.divide(r2), dimensions), RealScalar.ONE);
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return randomSampleInterface.randomSample(randomGenerator) //
        .multiply(power.apply(RandomVariate.of(distribution, randomGenerator)).multiply(r2));
  }
}
