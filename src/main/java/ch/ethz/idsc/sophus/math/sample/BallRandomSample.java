// code by jph
package ch.ethz.idsc.sophus.math.sample;

import java.io.Serializable;
import java.util.Random;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Clips;
import ch.ethz.idsc.tensor.sca.Sign;

/** uniform random samples from the interior of a n-dimensional sphere
 * the larger the dimension of the sphere the longer the sample generation may take.
 * Therefore the dimension is restricted to n <= 10.
 * 
 * implementation supports the use of Quantity
 * 
 * implementation generalizes {@link UniformRandomSample} and {@link DiskRandomSample}
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Ball.html">Ball</a> */
public class BallRandomSample implements RandomSampleInterface, Serializable {
  public static final int MAX_LENGTH = 10;
  private static final Distribution UNIFORM = UniformDistribution.of(Clips.absoluteOne());

  /** @param center non-empty vector of length less equals to 10
   * @param radius non-negative
   * @return
   * @throws Exception if center is not a vector
   * @throws Exception if radius is negative */
  public static RandomSampleInterface of(Tensor center, Scalar radius) {
    Sign.requirePositiveOrZero(radius);
    switch (center.length()) {
    case 0:
      throw TensorRuntimeException.of(center, radius);
    case 1: {
      Scalar middle = center.Get(0);
      Distribution distribution = UniformDistribution.of( //
          middle.subtract(radius), //
          middle.add(radius));
      return UniformRandomSample.of(distribution, 1);
    }
    case 2:
      return new DiskRandomSample(center, radius);
    }
    VectorQ.require(center);
    return Scalars.isZero(radius) //
        ? new ConstantRandomSample(center)
        : new BallRandomSample(center, radius);
  }

  // ---
  private final Tensor center;
  private final Scalar radius;

  private BallRandomSample(Tensor center, Scalar radius) {
    if (MAX_LENGTH < center.length())
      throw TensorRuntimeException.of(center);
    this.center = center;
    this.radius = radius;
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    while (true) {
      Tensor vector = RandomVariate.of(UNIFORM, random, center.length());
      if (Scalars.lessEquals(Norm._2.ofVector(vector), RealScalar.ONE))
        return vector.multiply(radius).add(center);
    }
  }
}
